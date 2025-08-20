package eu.highgeek.common;

import eu.highgeek.common.abstraction.CommonLogger;
import eu.highgeek.common.abstraction.CommonPlayer;
import eu.highgeek.common.abstraction.CommonPlugin;
import eu.highgeek.common.chat.ChannelManager;
import eu.highgeek.common.config.ConfigManager;
import eu.highgeek.common.economy.Currencies;
import eu.highgeek.common.events.EventBus;
import eu.highgeek.common.events.RabbitEvent;
import eu.highgeek.common.events.RedisEvent;
import eu.highgeek.common.events.RedisEventBus;
import eu.highgeek.common.rabbitmq.RabbitManager;
import eu.highgeek.common.redis.RedisManager;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommonMain {

    @Getter
    private static EventBus eventBus;
    @Getter
    private static RedisEventBus redisEventBus;

    @Getter
    private static CommonLogger logger;

    @Getter
    private static CommonMain commonMainInstance;

    @Getter
    private final CommonPlugin commonPlugin;

    @Getter
    private static RabbitManager rabbitManager;
    @Getter
    private static RedisManager redisManager;

    @Getter
    private static ChannelManager channelManager;
    @Getter
    private static final List<CommonPlayer> commonPlayers = new ArrayList<>();

    @Getter
    private static Currencies currencies;


    public CommonMain(CommonPlugin plugin){
        commonPlugin = plugin;
        commonMainInstance = this;
        logger = plugin.getCommonLogger();
        loadConfig();
        eventBus = plugin.getEventBus();
        redisEventBus = plugin.getRedisEventBus();
        redisManager = new RedisManager(this);
        rabbitManager = new RabbitManager(this);

        channelManager = new ChannelManager(redisManager);
        currencies = new Currencies();

    }


    public static CommonPlayer getCommonPlayer(UUID uuid){
        return getCommonPlayers().stream().filter(s -> s.getUniqueId().equals(uuid)).findAny().orElse(null);
    }
    public static CommonPlayer getCommonPlayer(String playerName){
        return getCommonPlayers().stream().filter(s -> s.getPlayerName().equals(playerName)).findAny().orElse(null);
    }


    private void loadConfig(){
        ConfigManager.setupConfig(logger, commonPlugin.getDataFolder());
    }

    public static void postEvent(Object event) {
        if (eventBus != null) {
            eventBus.postEvent(event);
        }
    }

    public static void postRedisEvent(RedisEvent event) {
        if (redisEventBus != null) {
            redisEventBus.postRedisEvent(event);
        }
    }

    public static void postRabbitEvent(RabbitEvent event) {
        if ( eventBus != null) {
            eventBus.postRabbitEvent(event);
        }
    }
}
