package eu.highgeek.paper.features.chat.channelitems;

import eu.highgeek.common.abstraction.CommonPlayer;
import eu.highgeek.common.objects.ChatChannel;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class LeaveChannelItem extends ItemStack implements ChannelItem {

    @Getter
    private final ChatChannel channel;

    private final CommonPlayer player;

    public LeaveChannelItem(ChatChannel channel, CommonPlayer player){
        super(Material.GREEN_WOOL);
        this.channel = channel;
        this.player = player;
        this.setLore(Arrays.asList("Currently listening in", channel.getFancyName()));
        ItemMeta meta = this.getItemMeta();
        meta.getPersistentDataContainer().set(NamespacedKey.fromString("channel"), PersistentDataType.STRING, channel.getName());
        this.setItemMeta(meta);
    }

    public ItemStack getItemStack(){
        return this;
    }

    public void onLeftClick(){
        player.disconnectFromChannel(channel);
    }

    public void onRightClick(){
        player.setChannelOut(channel);
    }
}
