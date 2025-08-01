package eu.highgeek.velocity.listeners;

import com.velocitypowered.api.event.AwaitingEventExecutor;

public interface Listener<E> extends AwaitingEventExecutor<E> {
    void register();
}
