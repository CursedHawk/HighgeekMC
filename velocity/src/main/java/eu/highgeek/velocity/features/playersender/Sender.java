package eu.highgeek.velocity.features.playersender;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.event.player.TabCompleteEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import eu.highgeek.common.CommonMain;
import eu.highgeek.common.abstraction.IPlayerSettings;
import eu.highgeek.common.config.ConfigManager;
import eu.highgeek.common.objects.PlayerAuthSuccessEvent;
import eu.highgeek.common.objects.PlayerSettings;
import eu.highgeek.common.redis.RedisManager;
import eu.highgeek.velocity.VelocityMain;
import eu.highgeek.velocity.impl.VelocityPlayer;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Sender {

    private final RedisManager redisManager = CommonMain.getRedisManager();

    private final List<String> authServers = ConfigManager.getPluginConfig().getStringList("auth-servers");
    private final List<String> forcedServers = ConfigManager.getPluginConfig().getStringList("forced-servers");
    private final Map<String, List<String>> virtualHosts = VelocityMain.getProxyServer().getConfiguration().getForcedHosts();
    @Getter
    private static final List<String> allowedCommands = ConfigManager.getPluginConfig().getStringList("allowed-commands");
    private final HashMap<String, String> hosts = new HashMap<>();;

    public Sender(){
        initHosts();
        for(String s : authServers){
            CommonMain.getLogger().debug("Auth server: " + s);
        }
        for(String s : forcedServers){
            CommonMain.getLogger().debug("Forced server: " + s);
        }
        for(String s : allowedCommands){
            CommonMain.getLogger().debug("Allowed commands: " + s);
        }

    }

    private RegisteredServer getServer(String name){
        return VelocityMain.getProxyServer().getServer(name).get();
    }

    @Subscribe
    public void playerChooseInitServerEvent(PlayerChooseInitialServerEvent event){
        String server = hosts.get(event.getPlayer().getRawVirtualHost().get());
        CommonMain.getLogger().debug("onPostLogin server is: " + server);
        if(forcedServers.contains(server)){
            //forced, stay on server
            CommonMain.getLogger().debug("modded, stay on server");
            event.setInitialServer(getServer(server));
        }else {
            CommonMain.getLogger().debug("connect to auth");
            event.setInitialServer(getServer(authServers.getFirst()));
        }
    }

    @Subscribe
    public void onPlayerAuthSuccess(PlayerAuthSuccessEvent event){
        if(CommonMain.getCommonPlayer(event.getPlayerName()) == null){
            Player player = VelocityMain.getProxyServer().getPlayer(event.getPlayerName()).get();
            VelocityPlayer velocityPlayer = new VelocityPlayer(player);
            velocityPlayer.setLogged(true);
            CommonMain.getCommonPlayers().add(velocityPlayer);
            String serverToConnect = determineServerToConnectAfterLogin(velocityPlayer.getPlayer().getRawVirtualHost().get(), velocityPlayer);
            if(serverToConnect != null){
                if(!serverToConnect.equals(player.getCurrentServer().get().getServerInfo().getName())){
                    connectPlayerToServer(velocityPlayer.getPlayer(), serverToConnect);
                }
            }
        }
    }
/*
    @Subscribe(order = PostOrder.FIRST, priority = 100)
    public EventTask onPlayerChatEvent(PlayerChatEvent event){
        return EventTask.withContinuation(continuation -> {
            Player player = event.getPlayer();
            if(CommonMain.getCommonPlayer(player.getUsername()) == null){
                sendAuthMessage(player);
                event.setResult(PlayerChatEvent.ChatResult.denied());
                return;
            }
            if(!CommonMain.getCommonPlayer(event.getPlayer().getUsername()).isLogged()){
                sendAuthMessage(player);
                event.setResult(PlayerChatEvent.ChatResult.denied());
                return;
            }
            continuation.resume();
        });
    }

    @Subscribe(order = PostOrder.CUSTOM, priority = 100)
    public EventTask onExecuteCommandEvent(CommandExecuteEvent event){
        return EventTask.withContinuation(continuation -> {
            CommonMain.getLogger().debug("First argument: " + getFirstArgument(event.getCommand()));
            if(event.getCommandSource() instanceof Player player){
                if(allowedCommands.contains(getFirstArgument(event.getCommand()))){
                    CommonMain.getLogger().debug("ALLOWED COMMAND");
                    continuation.resume();
                    return;
                }
                if(CommonMain.getCommonPlayer(player.getUsername()) == null){
                    CommonMain.getLogger().debug("DENIED COMMAND 1");
                    sendAuthMessage(player);
                    event.setResult(CommandExecuteEvent.CommandResult.denied());
                    return;
                }
                if(!CommonMain.getCommonPlayer(player.getUsername()).isLogged()){
                    CommonMain.getLogger().debug("DENIED COMMAND 2");
                    sendAuthMessage(player);
                    event.setResult(CommandExecuteEvent.CommandResult.denied());
                    return;
                }
            }
            CommonMain.getLogger().debug("ALLOWED");
            continuation.resume();
        });
    }

    @Subscribe(order = PostOrder.FIRST, priority = 100)
    public EventTask onTabCompleteEvent(TabCompleteEvent event){
        return EventTask.async(() -> {
            String command = event.getPartialMessage();
            for(String allowed : allowedCommands){
                CommonMain.getLogger().debug("TabComplete: " + allowed);
                if (allowed.startsWith(command)) {
                    return;
                }
            }
            if(CommonMain.getCommonPlayer(event.getPlayer().getUsername()) == null){
                event.getSuggestions().clear();
                return;
            }
            if(!CommonMain.getCommonPlayer(event.getPlayer().getUsername()).isLogged()){
                event.getSuggestions().clear();
                return;
            }
        });
    }
*/
    @Subscribe
    public void onServerSwitchEvent(ServerPostConnectEvent event){
        CommonMain.getLogger().debug("ServerPostConnectEvent");
        if(CommonMain.getCommonPlayer(event.getPlayer().getUsername()) != null){
            CommonMain.getLogger().debug("ServerPostConnectEvent != null");
            if(CommonMain.getCommonPlayer(event.getPlayer().getUsername()).isLogged()){
                //Player is logged, keep him logged
                CommonMain.getLogger().debug("Sending message to server player is already logged");
                redisManager.sendMessage("velocitylogin", redisManager.gson.toJson(new PlayerAuthSuccessEvent(event.getPlayer().getUniqueId(),event.getPlayer().getUsername())));
            }
        }
    }

    private void connectPlayerToServer(Player player, String serverName){
        CommonMain.getLogger().debug("connectPlayerToServer: " + serverName);
        Optional<RegisteredServer> server = VelocityMain.getProxyServer().getServer(serverName);
        server.ifPresent((target) -> {
                player.createConnectionRequest(target).fireAndForget();
            CommonMain.getLogger().debug("connectPlayerToServer target: " + target.getServerInfo().getName());
        });
    }

    private String determineServerToConnectAfterLogin(String host, IPlayerSettings playerSettings){
        String server = hosts.get(host);
        if(server == null){
            String lastServer = playerSettings.getPlayerSettingsFromRedis().lastServer;
            if(lastServer == null){
                return null;
            }else {
                return lastServer;
            }
        }else {
            return server;
        }
    }

    @Subscribe
    public void onPlayerLeave(DisconnectEvent event){
        IPlayerSettings iPlayerSettings = ((IPlayerSettings) CommonMain.getCommonPlayer(event.getPlayer().getUsername()));
        if(iPlayerSettings != null){
            PlayerSettings playerSettings = iPlayerSettings.getPlayerSettingsFromRedis();
            String toSet = event.getPlayer().getCurrentServer().get().getServer().getServerInfo().getName();
            if (!forcedServers.contains(toSet)){
                playerSettings.lastServer = toSet;
                iPlayerSettings.setPlayerSettingsInRedis(playerSettings);
            }
            CommonMain.getCommonPlayers().remove(CommonMain.getCommonPlayer(event.getPlayer().getUsername()));
        }
    }

    private void initHosts(){
        virtualHosts.forEach((key, value) ->{
            for (String val : value){
                hosts.put(key, val);
                CommonMain.getLogger().debug("added to hosts:\nkey: " +  key +"\nval: " + val);
            }
        });
    }

    public static void sendAuthMessage(Player player){
        //TODO language settings, playerSettings and redis impl?
        player.sendMessage(Component.text("You have to login first!", TextColor.fromHexString("#fc5454")));
    }
}

