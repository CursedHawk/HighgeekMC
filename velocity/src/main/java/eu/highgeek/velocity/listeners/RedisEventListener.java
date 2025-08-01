package eu.highgeek.velocity.listeners;

import eu.highgeek.common.CommonMain;
import eu.highgeek.common.events.RedisEvent;
import eu.highgeek.common.events.RedisEventBus;
import eu.highgeek.common.objects.PlayerAuthSuccessEvent;
import eu.highgeek.velocity.VelocityMain;

public class RedisEventListener implements RedisEventBus {

    @Override
    public void postRedisEvent(RedisEvent event) {
        CommonMain.getLogger().info("VelocityEventBus: Redis message on channel: " + event.getChannel() + " with message: " + event.getMessage());
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
            case "playerloggedevent":
                //playerloggedevent event
                VelocityMain.getProxyServer().getEventManager().fireAndForget(CommonMain.getRedisManager().gson.fromJson(event.getMessage(), PlayerAuthSuccessEvent.class));
                return;
            default:
                //every other event
                return;
        }
    }

    public static void setEvent(String uuid){
        String key = getRootKey(uuid);
        switch (key){
            case "chat":
                return;
            default:
                return;
        }

    }




    public static String getRootKey(String channel) {
        int index = channel.indexOf(':');

        if (index >= 0 && index < channel.length() - 1) {
            return channel.substring(0,index);
        }
        return channel;
    }
}
