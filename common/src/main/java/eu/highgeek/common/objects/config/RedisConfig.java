package eu.highgeek.common.objects.config;

import eu.highgeek.common.config.ConfigManager;
import lombok.Getter;

@Getter
public class RedisConfig {

    private String redisHost = "localhost";
    private int redisPort = 6379;

    public RedisConfig(){
        this.redisHost = ConfigManager.getPluginConfig().getString("redis.host");;
        this.redisPort = ConfigManager.getPluginConfig().getInt("redis.port");;
    }

}
