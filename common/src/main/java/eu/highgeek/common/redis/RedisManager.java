package eu.highgeek.common.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.highgeek.common.CommonMain;
import eu.highgeek.common.objects.ChatMessage;
import eu.highgeek.common.objects.RedisEconomyResponse;
import eu.highgeek.common.objects.config.RedisConfig;
import lombok.Getter;
import redis.clients.jedis.*;
import redis.clients.jedis.json.Path2;

import java.util.Set;

public class RedisManager {

    @Getter
    private final RedisListener redisListener;
    @Getter
    private final JedisPooled jedisPooled;
    @Getter
    private final UnifiedJedis unifiedJedis;

    private static final RedisConfig config = new RedisConfig();
    private static final HostAndPort node = HostAndPort.from(config.getRedisHost() + ":" + config.getRedisPort());
    private static final JedisClientConfig clientConfig = DefaultJedisClientConfig.builder()
            .resp3() // RESP3 protocol
            .build();

    public Gson gson = new GsonBuilder()
            .create();


    public RedisManager(CommonMain main){
        redisListener = new RedisListener(main.getCommonPlugin().getCommonLogger(), config);
        jedisPooled = new JedisPooled(config.getRedisHost(), config.getRedisPort());
        unifiedJedis = new UnifiedJedis(node, clientConfig);
        startListener();
    }

    public void startListener(){
        redisListener.start();
    }

    public void stopListener(){
        if (redisListener != null) redisListener.stopConsumer();
    }

    public String getStringRedis(String key){
        return jedisPooled.get(key);
    }

    public void setStringRedis(String key, String toSet){
        jedisPooled.set(key, toSet);
    }

    public Set<String> getKeysPrefix(String prefix){
        return jedisPooled.keys(prefix);
    }

    public void jsonSet(String uuid, String path, String toSet){
        jedisPooled.jsonSet(uuid, new Path2(path), toSet);
    }

    public void jsonSet(String uuid, String toSet){
        unifiedJedis.jsonSet(uuid, toSet);
    }

    public void deleteKey(String key){
        jedisPooled.del(key);
    }

    public void addChatEntry(ChatMessage message){
        jedisPooled.set(message.getUuid(), gson.toJson(message));
    }

    public Object jsonGet(String uuid){
        return unifiedJedis.jsonGet(uuid);
    }

    public void sendMessage(String channel, String message){
        jedisPooled.publish(channel, message);
    }


    public double getAccountBalance(String account, String id){
        return Double.parseDouble(getStringRedis("economy:accounts:"+account+":"+id));
    }

    public void setAccountBalance(String account, String id, double toSet){
        setStringRedis("economy:accounts:"+account+":"+id, String.valueOf(toSet));
    }

    public RedisEconomyResponse depositAccountBalance(String account, String id, double toDeposit){
        double current = getAccountBalance(account, id);
        setAccountBalance(account, id, current + toDeposit);
        return new RedisEconomyResponse(true, current + toDeposit, toDeposit);
    }

    public boolean hasAccountBalance(String account, String id, double amount){
        return (getAccountBalance(account, id) - amount ) >= 0;
    }

    public RedisEconomyResponse withdrawAccountBalance(String account, String id, double toWithdraw){
        double current = getAccountBalance(account, id);
        if(current >= 0){
            setAccountBalance(account, id, current - toWithdraw);
            return new RedisEconomyResponse(true, current - toWithdraw, toWithdraw);
        }
        return new RedisEconomyResponse(false, current, toWithdraw);
    }

    public boolean accountExists(String account, String currency){
        String ret =  getStringRedis("economy:accounts:" + account +":" + currency);
        return ret != null && !ret.trim().isEmpty();
    }
}
