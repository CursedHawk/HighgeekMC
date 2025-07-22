package eu.highgeek.common.redis;

import eu.highgeek.common.CommonMain;
import eu.highgeek.common.abstraction.CommonLogger;
import eu.highgeek.common.events.RedisEvent;
import eu.highgeek.common.objects.config.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisListener extends Thread {

    private final CommonLogger logger;
    private volatile boolean running = true;
    private final RedisConfig config;
    private final String channelName = "*";

    public RedisListener(CommonLogger logger, RedisConfig config){
        this.logger = logger;
        this.config = config;
    }

    @Override
    public void run() {
        while (running) {
            try (Jedis jedis = new Jedis(config.getRedisHost(), config.getRedisPort())) {
                logger.info("RedisListener: Připojeno k Redis serveru, poslouchám kanál '" + channelName + "'.");

                jedis.psubscribe(new JedisPubSub() {
                    @Override
                    public void onPMessage(String pattern, String channel, String message) {
                        if(message.contains("pterodactyl") || channel.contains("pterodactyl")){
                            return;
                        }
                        logger.debug("RedisListener: Redis message on channel: " + channel + " with message: " + message);
                        CommonMain.postRedisEvent(new RedisEvent(channel, message));
                    }
                }, channelName);

            } catch (Exception e) {
                logger.warn("RedisListener: Chyba spojení s Redisem: " + e.getMessage());
                e.printStackTrace();
                try {
                    Thread.sleep(5000); // Počkej 5 sekund před reconnectem
                } catch (InterruptedException ignored) {}
            }
        }
        logger.info("RedisListener: Ukončeno.");
    }

    public void stopConsumer() {
        running = false;
        this.interrupt(); // Pro případ, že čeká ve sleepu
    }
}
