package eu.highgeek.velocity.impl;

import eu.highgeek.common.events.EventBus;
import eu.highgeek.common.events.RabbitEvent;
import eu.highgeek.common.events.RedisEvent;
import eu.highgeek.common.redis.RedisManager;
import eu.highgeek.velocity.VelocityMain;

public class VelocityEventBus implements EventBus {

    private final VelocityMain velocityMain;

    public VelocityEventBus(VelocityMain proxyServer) {
        this.velocityMain = proxyServer;
    }

    @Override
    public void postEvent(Object event) {
    }

    @Override
    public void postRabbitEvent(RabbitEvent event) {

    }
}
