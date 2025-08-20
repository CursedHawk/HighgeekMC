package eu.highgeek.paper.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import eu.highgeek.common.config.ConfigManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class DebugCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("paperdebug")
                .executes(DebugCommand::toggleDebug);
    }


    private static int toggleDebug(CommandContext<CommandSourceStack> ctx){
        ConfigManager.toggleDebug();
        return Command.SINGLE_SUCCESS;
    }
}
