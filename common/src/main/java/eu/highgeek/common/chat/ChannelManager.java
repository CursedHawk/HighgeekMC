package eu.highgeek.common.chat;

import eu.highgeek.common.CommonMain;
import eu.highgeek.common.abstraction.CommonPlayer;
import eu.highgeek.common.objects.ChatChannel;
import eu.highgeek.common.redis.RedisManager;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChannelManager {

    @Getter
    private final List<ChatChannel> chatChannels = new ArrayList<>();

    private final RedisManager redisManager;

    public ChannelManager(RedisManager redisManager){
        this.redisManager = redisManager;
        initChannels();
    }

    private void initChannels(){
        Set<String> keySet = redisManager.getKeysPrefix("settings:server:chat:channels:*");

        for (String key : keySet) {
            String channelJson = redisManager.getStringRedis(key);
            ChatChannel channel = redisManager.gson.fromJson(channelJson, ChatChannel.class);
            chatChannels.add(channel);
            CommonMain.getLogger().debug("Channel loaded: " + channel.name);
        }

    }

    public ChatChannel getChatChannelFromName(String name){
        ChatChannel channel = chatChannels.stream()
                .filter(inv -> inv.name.equals(name))
                .findAny()
                .orElse(null);

        return channel;
    }

    public List<ChatChannel> getDefaultChatChannels(){
        List<ChatChannel> channels = chatChannels.stream()
                .filter(channel -> channel.isDefault())
                .collect(Collectors.toList());
        return channels;
    }

}
