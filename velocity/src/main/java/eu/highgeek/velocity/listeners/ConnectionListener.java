package eu.highgeek.velocity.listeners;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import eu.highgeek.common.CommonMain;
import eu.highgeek.common.objects.ChatMessage;

import java.time.Instant;
import java.util.UUID;

public class ConnectionListener {


    @Subscribe
    public void onPlayerJoin(LoginEvent event){
        //CommonMain.getCommonPlayers().add(new VelocityPlayer(event.getPlayer()));
    }


    @Subscribe
    public EventTask onPlayerChat(PlayerChooseInitialServerEvent event) {
        return EventTask.async(() -> processAsyncLoginEvent(event));
    }


    public void processAsyncLoginEvent(PlayerChooseInitialServerEvent event){
        String time =  Instant.now().toString();
        String uuid = "chat:logs:"+time.replaceAll(":", "-")+":server";

        CommonMain.getRedisManager().addChatEntry(new ChatMessage(
                uuid,
                event.getPlayer().getUsername(),
                event.getPlayer().getUsername(),
                "se pripojil k serveru.",
                "default",
                time,
                "logs",
                "&7Login",
                "game",
                "",
                "&7",
                "&f",
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                "&7Net",
                false,
                null));
    }


    @Subscribe
    public EventTask onPlayerChat(DisconnectEvent event) {
        return EventTask.async(() -> processAsyncDisconnectEvent(event));
    }
    public void processAsyncDisconnectEvent(DisconnectEvent event){
        String time =  Instant.now().toString();
        String uuid = "chat:logs:"+time.replaceAll(":", "-")+":server";

        CommonMain.getRedisManager().addChatEntry(
                new ChatMessage(
                        uuid,
                        event.getPlayer().getUsername(),
                        event.getPlayer().getUsername(),
                        "se odpojil ze serveru.",
                        "default",
                        time,
                        "logs",
                        "&7Login",
                        "game",
                        "",
                        "&7",
                        "&f",
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        "&7Net",
                        false,
                        null));
    }
}
