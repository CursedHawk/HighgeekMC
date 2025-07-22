package eu.highgeek.paper.features.chat.channelitems;

import eu.highgeek.common.objects.ChatChannel;
import org.bukkit.inventory.ItemStack;

public interface ChannelItem {
    void onLeftClick();
    void onRightClick();
    ItemStack getItemStack();
    ChatChannel getChannel();
}
