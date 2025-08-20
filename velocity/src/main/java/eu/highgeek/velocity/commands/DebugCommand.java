package eu.highgeek.velocity.commands;

import com.velocitypowered.api.command.RawCommand;
import eu.highgeek.common.config.ConfigManager;

public class DebugCommand implements RawCommand {

    @Override
    public void execute(final Invocation invocation) {
        ConfigManager.toggleDebug();
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("highgeek.debug");
    }
}
