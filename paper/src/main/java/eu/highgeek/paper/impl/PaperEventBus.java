package eu.highgeek.paper.impl;

import eu.highgeek.common.CommonMain;
import eu.highgeek.common.events.EventBus;
import eu.highgeek.common.events.RabbitEvent;
import eu.highgeek.common.events.RedisEvent;
import eu.highgeek.paper.PaperMain;
import eu.highgeek.paper.listeners.RedisEventListener;

public class PaperEventBus implements EventBus {


    public PaperEventBus(){

    }

    @Override
    public void postEvent(Object event) {
    }

    @Override
    public void postRabbitEvent(RabbitEvent event) {

    }

}
