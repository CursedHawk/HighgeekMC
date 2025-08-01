package eu.highgeek.velocity.listeners.inputs;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import eu.highgeek.common.CommonMain;
import eu.highgeek.velocity.VelocityMain;
import eu.highgeek.velocity.features.playersender.Sender;
import eu.highgeek.velocity.listeners.Listener;

public class ChatListener implements Listener<PlayerChatEvent> {

    @Inject
    private VelocityMain plugin;
    @Inject
    private EventManager eventManager;

    @Override
    public void register() {
        eventManager.register(plugin, PlayerChatEvent.class, PostOrder.FIRST, this);
    }

    @Override
    public EventTask executeAsync(final PlayerChatEvent event) {
        return EventTask.withContinuation(continuation -> {
            Player player = event.getPlayer();
            if(CommonMain.getCommonPlayer(player.getUsername()) == null){
                Sender.sendAuthMessage(player);
                event.setResult(PlayerChatEvent.ChatResult.denied());
                return;
            }
            if(!CommonMain.getCommonPlayer(event.getPlayer().getUsername()).isLogged()){
                Sender.sendAuthMessage(player);
                event.setResult(PlayerChatEvent.ChatResult.denied());
                return;
            }
            continuation.resume();
        });
    }
}