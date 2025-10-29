package eu.highgeek.paper.integration.authme;

import eu.highgeek.common.CommonMain;
import eu.highgeek.common.objects.PlayerAuthSuccessEvent;
import eu.highgeek.common.redis.RedisManager;
import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AuthMeListener implements Listener {

    private final RedisManager redisManager = CommonMain.getRedisManager();

    public AuthMeListener(){

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLogin(final LoginEvent event) {
        final Player player = event.getPlayer();
        CommonMain.getCommonPlayer(event.getPlayer().getUniqueId()).setLogged(true);
        redisManager.sendMessage("playerloggedevent", redisManager.gson.toJson(new PlayerAuthSuccessEvent(player.getUniqueId(), player.getName())));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSessionRestored(RestoreSessionEvent event) {
        final Player player = event.getPlayer();

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onRegister(final RegisterEvent event) {
        final Player player = event.getPlayer();
        CommonMain.getCommonPlayer(event.getPlayer().getUniqueId()).setLogged(true);
        redisManager.sendMessage("playerloggedevent", redisManager.gson.toJson(new PlayerAuthSuccessEvent(player.getUniqueId(), player.getName())));
    }

    @EventHandler
    public void onLogout(final LogoutEvent event) {

    }

    @EventHandler
    public void onUnRegister(final UnregisterByPlayerEvent event) {

    }

    @EventHandler
    public void onAdminUnRegister(final UnregisterByAdminEvent event) {

    }

    public void forcePlayerLogin(PlayerAuthSuccessEvent event){
        Player player = Bukkit.getPlayer(event.getPlayerUuid());
        if(player != null) {
            AuthMeApi.getInstance().forceLogin(player);
        }
    }
}
