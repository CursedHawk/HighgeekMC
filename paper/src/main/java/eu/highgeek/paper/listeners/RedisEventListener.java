package eu.highgeek.paper.listeners;

import eu.highgeek.common.CommonMain;
import eu.highgeek.common.abstraction.CommonPlayer;
import eu.highgeek.common.events.RedisEvent;
import eu.highgeek.common.events.RedisEventBus;
import eu.highgeek.common.objects.ChatMessage;
import eu.highgeek.common.objects.PlayerSettings;
import eu.highgeek.paper.PaperMain;
import eu.highgeek.paper.features.chat.ChatEvent;
import org.bukkit.Bukkit;

public class RedisEventListener implements RedisEventBus {

    public RedisEventListener(){
    }



    @Override
    public void postRedisEvent(RedisEvent event) {
        CommonMain.getLogger().info("PaperEventBus: Redis message on channel: " + event.getChannel() + " with message: " + event.getMessage());
        switch (event.getChannel()){
            case "__keyevent@0__:set":
                //set event
                setEvent(event.getMessage());
                return;
            case "__keyevent@0__:del":
                //del event
                return;
            case "__keyevent@0__:expire":
                //expire event
                return;
            default:
                //every other event
                return;
        }
    }

    public static void setEvent(String uuid){
        String key = getKey(uuid);
        switch (key){
            case "chat":
                if(!uuid.startsWith("chat:items")){
                    try {
                        Bukkit.getServer().getPluginManager().callEvent(new ChatEvent(CommonMain.getRedisManager().gson.fromJson( CommonMain.getRedisManager().getStringRedis(uuid), ChatMessage.class), true));
                    }catch (Exception e){
                        CommonMain.getLogger().info("Failed to send message: " + uuid);
                        e.printStackTrace();
                    }
                }
                return;
            case "players":
                firePlayersEvent(uuid);
                return;

            default:
                return;
        }

    }


    public void delEvent(String uuid){

    }

    public void expireEvent(String uuid){

    }


    public static void firePlayersEvent(String uuid){
        if(uuid.contains("settings")){
            String playerName = uuid.substring(uuid.lastIndexOf(':') + 1, uuid.length());
            CommonPlayer player = CommonMain.getCommonPlayer(playerName);
            if(player != null){
                //PlayerSettings newPlayerSettings = CommonMain.getRedisManager().gson.fromJson(CommonMain.getRedisManager().getStringRedis(uuid), PlayerSettings.class);
                //newPlayerSettings.player = player;
                //player.setPlayerSettings(newPlayerSettings);
                if(PaperMain.getOpenedChannelMenus().containsKey(player.getPlayerName())){
                    PaperMain.getOpenedChannelMenus().get(player.getPlayerName()).init();
                }
            }
        }
    }




    public static String getKey(String channel) {
        int index = channel.indexOf(':');

        if (index >= 0 && index < channel.length() - 1) {
            return channel.substring(0,index);
        }
        return channel;
    }
}
