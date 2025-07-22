package eu.highgeek.paper.features.chat;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import eu.highgeek.common.CommonMain;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ChannelCommand {


    public static LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("chan")
                .executes(ChannelCommand::openChannelMenu);
    }

    private static int openChannelMenu(CommandContext<CommandSourceStack> ctx){

        CommandSender sender = ctx.getSource().getSender();
        Entity executor = ctx.getSource().getExecutor();

        if (!(executor instanceof Player player)) {
            sender.sendPlainMessage("Only players can open channel menus!");
            return Command.SINGLE_SUCCESS;
        }

        player.openInventory(new ChannelMenu(CommonMain.getCommonPlayer(player.getUniqueId())).getInventory());

        return Command.SINGLE_SUCCESS;
    }
}
