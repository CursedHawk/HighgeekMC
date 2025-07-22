package eu.highgeek.paper.features.chat;

import eu.highgeek.common.CommonMain;
import eu.highgeek.common.abstraction.CommonPlayer;
import eu.highgeek.common.objects.ChatChannel;
import eu.highgeek.paper.PaperMain;
import eu.highgeek.paper.features.chat.channelitems.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ChannelMenu implements InventoryHolder, Listener {

    private final Inventory inventory;
    private final CommonPlayer player;
    private final HashMap<String, ChannelItem> items = new HashMap();

    public ChannelMenu(CommonPlayer player){
        this.player = player;
        this.inventory = Bukkit.createInventory(this, 9, "Channel Selector");
        Bukkit.getServer().getPluginManager().registerEvents(this, PaperMain.getMainInstance());
        PaperMain.getOpenedChannelMenus().put(player.getPlayerName(), this);
        init();
    }

    public void init(){
        inventory.clear();
        items.clear();
        inventory.addItem(getInfoItem());
        for(ChatChannel channel : CommonMain.getChannelManager().getChatChannels()){
            ChannelItem i = generateItemStack(channel);
            items.put(channel.getName(), i);
            inventory.addItem(i.getItemStack());
        }
    }

    private ChannelItem generateItemStack(ChatChannel chatChannel){
        if(Objects.equals(player.getChannelOut(), chatChannel)){
            return new OutChannelItem(chatChannel, player);
        }else
        if(player.getListeningChannels().contains(chatChannel)){
            return new LeaveChannelItem(chatChannel, player);
        }else {
            return new JoinChannelItem(chatChannel, player);
        }
    }

    private ItemStack getDiscordChannelItem(){
        if(player.getPlayerSettingsFromRedis().hasConnectedDiscord){
            ItemStack itemStack = new ItemStack(Material.BLUE_WOOL);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName("Discord");
            meta.setLore(Arrays.asList("Integration is connected"));
            itemStack.setItemMeta(meta);
            return itemStack;
        }else{
            ItemStack itemStack = new ItemStack(Material.RED_WOOL);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName("Discord");
            //TODO getDiscordLinkingCode on CommonPlayer
            //meta.setLore(Arrays.asList("Integration is disconnected.", "To link your game account with Discord", "send Direct Message to our Discord bot ", "with code: "+ this.player.getDiscordLinkingCode().getCode()));
            itemStack.setItemMeta(meta);
            return itemStack;
        }
    }

    private ItemStack getInfoItem(){
        ItemStack itemStack = new ItemStack(Material.WHITE_WOOL);

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("Usage?");

        meta.setLore(Arrays.asList("BLUE - channel you are talking in",
                "GREEN - channel you are listening to",
                "RED - channel you are not listening to",
                "LEFT click - listen/leave channel",
                "RIGHT click - set talking channel"));
        itemStack.setItemMeta(meta);

        return itemStack;
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        CommonMain.getLogger().debug("onInventoryClick start");
        if(event.getInventory().getHolder() == this){
            CommonMain.getLogger().debug("onInventoryClick this");
            ItemStack cur = event.getCurrentItem();
            event.setCancelled(true);
            if(cur != null){
                if(cur.getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("channel"), PersistentDataType.STRING) != null){
                    CommonMain.getLogger().debug("onInventoryClick item");
                    String ch = cur.getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("channel"), PersistentDataType.STRING);
                    ChannelItem item = items.get(ch);
                    if(event.isRightClick()){
                        CommonMain.getLogger().debug("onInventoryClick right");
                        item.onRightClick();
                    }
                    if(event.isLeftClick()){
                        CommonMain.getLogger().debug("onInventoryClick left");
                        item.onLeftClick();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent event){
        if(event.getInventory().getHolder() == this){
            PaperMain.getOpenedChannelMenus().remove(player.getPlayerName());
        }
    }


    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
