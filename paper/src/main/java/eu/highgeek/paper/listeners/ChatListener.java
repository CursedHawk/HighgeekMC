package eu.highgeek.paper.listeners;

import eu.highgeek.common.CommonMain;
import eu.highgeek.common.abstraction.CommonPlayer;
import eu.highgeek.common.config.ConfigManager;
import eu.highgeek.paper.PaperMain;
import eu.highgeek.paper.adapters.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import eu.highgeek.common.objects.ChatChannel;
import eu.highgeek.common.objects.ChatMessage;
import eu.highgeek.common.redis.RedisManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.util.StringUtil;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import java.time.Instant;
import java.util.UUID;


public class ChatListener implements Listener {

    public static final String servername = ConfigManager.getPluginConfig().getString("server-name");
    public static final String prettyServerName =  ConfigManager.getPluginConfig().getString("pretty-server-name");
    private final RedisManager redisManager = CommonMain.getRedisManager();


    @EventHandler
    void onPlayerAsyncChatEvent(AsyncPlayerChatEvent event){
        setRedisMessageAsync(event);
        event.setCancelled(true);
    }

    public void setRedisMessageAsync(AsyncPlayerChatEvent event){
        Bukkit.getScheduler().runTaskAsynchronously(PaperMain.getMainInstance(), new Runnable() {
            @Override
            public void run() {
                String playerName = event.getPlayer().getName();
                String time =  Instant.now().toString();

                ChatChannel chatChannel = CommonMain.getCommonPlayer(event.getPlayer().getUniqueId()).getChannelOut();

                String uuid = "chat:"+chatChannel.getName()+":"+time.replaceAll(":", "-")+":"+playerName;
                String channelPrefix = chatChannel.getPrefix();

                String prefix = PlaceholderAPI.setPlaceholders(event.getPlayer(), "%vault_prefix%");
                String suffix = PlaceholderAPI.setPlaceholders(event.getPlayer(), "%vault_suffix%");;
                String primaryGroup = PlaceholderAPI.setPlaceholders(event.getPlayer(),"%luckperms_primary_group_name%");

                if(event.getMessage().contains("[item]")){
                    if(event.getPlayer().getItemInHand().getType() == Material.AIR){
                        event.getPlayer().sendMessage("You cannot send empty hand!");
                        return;
                    }
                    String itemAddress = "chat:items:" + UUID.randomUUID();
                    redisManager.setStringRedis(itemAddress, ItemStackUtils.toString(event.getPlayer().getItemInHand()));
                    ChatMessage message = new ChatMessage(uuid, playerName, playerName, event.getMessage(), primaryGroup, time, chatChannel.getName(), channelPrefix, "game", servername, prefix, suffix, event.getPlayer().getUniqueId(), prettyServerName, true, itemAddress);
                    CommonMain.getLogger().debug("ChatMessage: " + message.toString());
                    redisManager.addChatEntry(message);
                }else {
                    redisManager.addChatEntry(new ChatMessage(uuid, playerName, playerName, event.getMessage(), primaryGroup, time, chatChannel.getName(), channelPrefix, "game", servername, prefix, suffix, event.getPlayer().getUniqueId(), prettyServerName, false, null));
                }

            }
        });
    }



}
