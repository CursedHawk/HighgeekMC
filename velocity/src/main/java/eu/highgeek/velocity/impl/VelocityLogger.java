package eu.highgeek.velocity.impl;


import eu.highgeek.common.abstraction.CommonLogger;
import eu.highgeek.common.config.ConfigManager;
import org.slf4j.Logger;


public class VelocityLogger implements CommonLogger {

    private final Logger logger;
    public VelocityLogger(Logger logger){
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void severe(String message) {
        logger.error(message);
    }

    @Override
    public void debug(String message) {
        if(ConfigManager.getPluginConfig().getBoolean("debug")){
            logger.info("Debug: {}", message);
        }
    }

    @Override
    public boolean debug(){
        return ConfigManager.getPluginConfig().getBoolean("debug");
    }
}

