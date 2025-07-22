package eu.highgeek.common.events;

public interface RedisEventBus {
    void postRedisEvent(RedisEvent event);
}
