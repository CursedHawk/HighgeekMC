package eu.highgeek.velocity.listeners.inputs;

import com.velocitypowered.api.event.command.CommandExecuteEvent;
import eu.highgeek.common.CommonMain;
import eu.highgeek.velocity.VelocityMain;
import eu.highgeek.velocity.features.playersender.Sender;
import eu.highgeek.velocity.listeners.Listener;
import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CommandListener implements Listener<CommandExecuteEvent> {

    @Inject
    private EventManager eventManager;
    @Inject
    private VelocityMain plugin;

    @Override
    public void register() {
        eventManager.register(plugin, CommandExecuteEvent.class, PostOrder.FIRST, this);
    }


    @Override
    public EventTask executeAsync(final CommandExecuteEvent event){
        return EventTask.withContinuation(continuation -> {
            CommonMain.getLogger().debug("First argument: " + getFirstArgument(event.getCommand()));
            if(event.getCommandSource() instanceof Player player){
                if(Sender.getAllowedCommands().contains(getFirstArgument(event.getCommand()))){
                    CommonMain.getLogger().debug("ALLOWED COMMAND");
                    //TODO AFTER ONE COMMAND OR ANY CHAT INTERACTION THIS BROKE AND CANNOT RESUME
                    continuation.resume();
                    return;
                }
                if(CommonMain.getCommonPlayer(player.getUsername()) == null){
                    CommonMain.getLogger().debug("DENIED COMMAND 1");
                    Sender.sendAuthMessage(player);
                    event.setResult(CommandExecuteEvent.CommandResult.denied());
                    return;
                }
                if(!(CommonMain.getCommonPlayer(player.getUsername()).isLogged())){
                    CommonMain.getLogger().debug("DENIED COMMAND 2");
                    Sender.sendAuthMessage(player);
                    event.setResult(CommandExecuteEvent.CommandResult.denied());
                    return;
                }
            }
            CommonMain.getLogger().debug("ALLOWED");

            continuation.resume();
        });
    }


    public static @NotNull String getFirstArgument(@NotNull String string){
        int index = Objects.requireNonNull(string).indexOf(' ');
        if (index == -1) {
            return string;
        }
        return string.substring(0, index);
    }
}
