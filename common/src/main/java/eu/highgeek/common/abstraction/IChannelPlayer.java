package eu.highgeek.common.abstraction;

import eu.highgeek.common.objects.ChatChannel;

import java.util.List;

public interface IChannelPlayer {
    List<ChatChannel> getListeningChannels();
    ChatChannel getChannelOut();

    void joinToChannel(ChatChannel channel);
    void disconnectFromChannel(ChatChannel channel);
    void setChannelOut(ChatChannel channel);
}
