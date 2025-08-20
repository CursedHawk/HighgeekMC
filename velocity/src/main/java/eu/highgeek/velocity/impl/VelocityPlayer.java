package eu.highgeek.velocity.impl;

import com.velocitypowered.api.proxy.Player;
import eu.highgeek.common.CommonMain;
import eu.highgeek.common.abstraction.CommonPlayer;
import eu.highgeek.common.abstraction.IPlayerSettings;
import eu.highgeek.common.economy.Currency;
import eu.highgeek.common.objects.ChatChannel;
import eu.highgeek.common.objects.PlayerSettings;
import eu.highgeek.common.redis.RedisManager;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class VelocityPlayer implements CommonPlayer, IPlayerSettings {

    private final RedisManager redisManager = CommonMain.getRedisManager();

    public VelocityPlayer(Player player){
        this.player = player;
        createEconomy();
    }

    @Setter
    @Getter
    private boolean logged = false;

    @Getter
    private final Player player;

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public String getPlayerName() {
        return player.getUsername();
    }

    public void setPlayerSettingsInRedis(PlayerSettings playerSettings){
        redisManager.setStringRedis("players:settings:"+playerSettings.playerName, redisManager.gson.toJson(playerSettings));
    }

    public PlayerSettings getPlayerSettingsFromRedis(){
        String playerSettings = redisManager.getStringRedis("players:settings:"+player.getUsername());
        if (playerSettings == null){
            PlayerSettings newPlayerSettings = new PlayerSettings(player.getUsername(), player.getUniqueId().toString(), CommonMain.getChannelManager().getDefaultChatChannels().stream().map(ChatChannel::getName).collect(Collectors.toList()), "global", false, new ArrayList<>());
            setPlayerSettingsInRedis(newPlayerSettings);
            return newPlayerSettings;
        }else{
            return redisManager.gson.fromJson(playerSettings, PlayerSettings.class);
        }
    }

    public void setLastServer(String lastServer){
        PlayerSettings settings = getPlayerSettingsFromRedis();
        settings.lastServer = lastServer;
        setPlayerSettingsInRedis(settings);
    }

    public String getLastServer(){
        return getPlayerSettingsFromRedis().lastServer;
    }

    public void createEconomy(){
        for (Currency currency : CommonMain.getCurrencies().getCurrencies().values()){
            if(!currency.accountExists("players:" + player.getUsername())){
                currency.setAccountCurrency("players:" + player.getUsername(), Double.parseDouble(currency.getDefaultValue()));
            }
        }
    }
}
