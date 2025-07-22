package eu.highgeek.paper.impl;

import eu.highgeek.common.CommonMain;
import eu.highgeek.common.abstraction.CommonPlayer;
import eu.highgeek.common.objects.ChatChannel;
import eu.highgeek.common.objects.PlayerSettings;
import eu.highgeek.common.redis.RedisManager;
import eu.highgeek.paper.PaperMain;
import eu.highgeek.paper.features.chat.ChatEvent;
import eu.highgeek.paper.features.chat.MessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PaperPlayer implements CommonPlayer, Listener {

    private final Player player;
    private final RedisManager redisManager = CommonMain.getRedisManager();


    public PaperPlayer(Player player){
        this.player = player;
        Bukkit.getServer().getPluginManager().registerEvents(this, PaperMain.getMainInstance());
    }

    @EventHandler
    public void onChatMessage(ChatEvent event){
        if(getListeningChannels().contains(event.getChannel())){
            CommonMain.getLogger().debug("Message delivered to " + player.getName());
            player.sendMessage(MessageBuilder.createChatComponent(event.getMessage()));
        }
    }

    public void setPlayerSettingsInRedis(PlayerSettings playerSettings){
        redisManager.setStringRedis("players:settings:"+playerSettings.playerName, redisManager.gson.toJson(playerSettings));
    }



    public PlayerSettings getPlayerSettingsFromRedis(){
        String playerSettings = redisManager.getStringRedis("players:settings:"+player.getName());
        if (playerSettings == null){
            PlayerSettings newPlayerSettings = new PlayerSettings(player.getName(), player.getUniqueId().toString(), CommonMain.getChannelManager().getDefaultChatChannels().stream().map(ChatChannel::getName).collect(Collectors.toList()), "global", false, new ArrayList<>());
            setPlayerSettingsInRedis(newPlayerSettings);
            return newPlayerSettings;
        }else{
            return redisManager.gson.fromJson(playerSettings, PlayerSettings.class);
        }
    }


    @Override
    public List<ChatChannel> getListeningChannels() {
        List<ChatChannel> channels = new ArrayList<>();
        for (String s : getPlayerSettings().joinedChannels){
            channels.add(CommonMain.getChannelManager().getChatChannelFromName(s));
        }
        return channels;
    }

    @Override
    public ChatChannel getChannelOut() {
        return CommonMain.getChannelManager().getChatChannelFromName(getPlayerSettings().channelOut);
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public String getPlayerName() {
        return player.getName();
    }

    @Override
    public PlayerSettings getPlayerSettings() {
        return getPlayerSettingsFromRedis();
    }

    @Override
    public void joinToChannel(ChatChannel channel) {
        PlayerSettings playerSettings = getPlayerSettingsFromRedis();
        playerSettings.joinedChannels.add(channel.name);
        setPlayerSettingsInRedis(playerSettings);
    }

    @Override
    public void disconnectFromChannel(ChatChannel channel) {
        CommonMain.getLogger().debug("disconnectFromChannel");
        PlayerSettings playerSettings = getPlayerSettingsFromRedis();
        if(playerSettings.joinedChannels.contains(channel.name)){
            CommonMain.getLogger().debug("disconnectFromChannel remove");
            playerSettings.joinedChannels.remove(channel.name);
            setPlayerSettingsInRedis(playerSettings);
        }
    }

    @Override
    public void setChannelOut(ChatChannel channel) {
        PlayerSettings playerSettings = getPlayerSettingsFromRedis();
        playerSettings.channelOut = channel.name;
        joinToChannel(channel);
        setPlayerSettingsInRedis(playerSettings);
    }

    public void onDisconnect(){

    }



}
