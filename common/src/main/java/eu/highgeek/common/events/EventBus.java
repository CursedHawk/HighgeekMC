package eu.highgeek.common.events;

public interface EventBus {
    void postEvent(Object event);
    void postRabbitEvent(RabbitEvent event);
}
