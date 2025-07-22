package eu.highgeek.paper.impl;

import eu.highgeek.common.abstraction.CommonLogger;
import eu.highgeek.common.config.ConfigManager;

import java.util.logging.Logger;

public class PaperLogger implements CommonLogger {

    private final Logger logger;
    public PaperLogger(Logger logger){
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warning(message);
    }

    @Override
    public void severe(String message) {
        logger.severe(message);
    }

    @Override
    public void debug(String message) {
        if(ConfigManager.getPluginConfig().getBoolean("debug")){
            logger.info("Debug: " + message);
        }
    }

    @Override
    public boolean debug(){
        return ConfigManager.getPluginConfig().getBoolean("debug");
    }
}
