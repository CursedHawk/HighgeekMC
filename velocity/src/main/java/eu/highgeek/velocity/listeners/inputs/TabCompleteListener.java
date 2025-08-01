package eu.highgeek.velocity.listeners.inputs;

import com.velocitypowered.api.event.player.TabCompleteEvent;
import eu.highgeek.common.CommonMain;
import eu.highgeek.velocity.VelocityMain;
import eu.highgeek.velocity.features.playersender.Sender;
import eu.highgeek.velocity.listeners.Listener;
import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;

public class TabCompleteListener implements Listener<TabCompleteEvent> {

    @Inject
    private VelocityMain plugin;
    @Inject
    private EventManager eventManager;

    @Override
    public void register() {
        eventManager.register(plugin, TabCompleteEvent.class, PostOrder.FIRST, this);
    }

    @Override
    public EventTask executeAsync(final TabCompleteEvent event) {
        CommonMain.getLogger().debug("TabComplete event");
        return EventTask.async(() -> {
            String command = event.getPartialMessage();
            for(String allowed : Sender.getAllowedCommands()){
                if (allowed.startsWith(command)) {
                    return;
                }
            }
            if(CommonMain.getCommonPlayer(event.getPlayer().getUsername()) == null){
                event.getSuggestions().clear();
                return;
            }
            if(!CommonMain.getCommonPlayer(event.getPlayer().getUsername()).isLogged()){
                event.getSuggestions().clear();
                return;
            }
        });
    }
}
