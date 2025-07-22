package eu.highgeek.common.events;

import lombok.Getter;

@Getter
public class RedisEvent {

    private String channel;
    private String message;

    public RedisEvent(String channel, String message){
        this.channel = channel;
        this.message = message;
    }
}
