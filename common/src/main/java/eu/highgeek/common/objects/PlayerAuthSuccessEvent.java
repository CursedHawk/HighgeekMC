package eu.highgeek.common.objects;

import lombok.Getter;

import java.util.UUID;

public class PlayerAuthSuccessEvent {

    @Getter
    private final UUID playerUuid;
    @Getter
    private final String playerName;


    public PlayerAuthSuccessEvent(UUID uuid, String playerName){
        this.playerName = playerName;
        this.playerUuid = uuid;
    }
}
