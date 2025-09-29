package eu.highgeek.paper.features.chat.channelitems;

import eu.highgeek.common.abstraction.IChannelPlayer;
import eu.highgeek.common.abstraction.CommonPlayer;
import eu.highgeek.common.objects.ChatChannel;
import eu.highgeek.paper.impl.PaperPlayer;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class JoinChannelItem extends ItemStack implements ChannelItem {

    @Getter
    private final ChatChannel channel;

    private final CommonPlayer player;

    public JoinChannelItem(ChatChannel channel, CommonPlayer player){
        super(Material.RED_WOOL);
        this.channel = channel;
        this.player = player;
        this.setLore(Arrays.asList("Click to join channel", channel.getFancyName()));
        ItemMeta meta = this.getItemMeta();
        meta.getPersistentDataContainer().set(NamespacedKey.fromString("channel"), PersistentDataType.STRING, channel.getName());
        this.setItemMeta(meta);
    }

    public ItemStack getItemStack(){
        return this;
    }

    public void onLeftClick(){
        ((IChannelPlayer) player).joinToChannel(channel);
    }

    public void onRightClick(){
        if(player.checkPermission(channel.speakPermission)){
            ((IChannelPlayer) player).setChannelOut(channel);
            if (!((IChannelPlayer) player).getListeningChannels().contains(channel)){
                ((IChannelPlayer) player).joinToChannel(channel);
            }
        }else {
            ((PaperPlayer) player).getPlayer().sendMessage("You cannot talk in this channel.");
        }
    }
}
