package eu.highgeek.common.objects.config;

import eu.highgeek.common.config.ConfigManager;
import lombok.Getter;

@Getter
public class RabbitConfig {
    private final String rabbitHost;
    private final String rabbitUsername;
    private final String rabbitPassword;

    public RabbitConfig(){
        this.rabbitHost = ConfigManager.getPluginConfig().getString("rabbit.host");
        this.rabbitUsername = ConfigManager.getPluginConfig().getString("rabbit.username");
        this.rabbitPassword = ConfigManager.getPluginConfig().getString("rabbit.password");
    }
}
