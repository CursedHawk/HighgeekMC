package eu.highgeek.paper.listeners;

import eu.highgeek.common.CommonMain;
import eu.highgeek.common.abstraction.CommonPlayer;
import eu.highgeek.common.config.ConfigManager;
import eu.highgeek.paper.impl.PaperPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    public static final String servername = ConfigManager.getPluginConfig().getString("server-name");

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        PaperPlayer newPlayer = new PaperPlayer(event.getPlayer());
        CommonMain.getCommonPlayers().add(newPlayer);
        if(CommonMain.getRedisManager().jsonGet("players:stats:" + event.getPlayer().getName() + ":" + servername) == null){
            StatsListener.createNewStatsCounter(event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        CommonPlayer player = CommonMain.getCommonPlayer(event.getPlayer().getUniqueId());
        if(player != null){
            CommonMain.getCommonPlayers().remove(player);
        }
    }
}
