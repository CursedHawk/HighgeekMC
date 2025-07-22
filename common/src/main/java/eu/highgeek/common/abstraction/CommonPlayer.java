package eu.highgeek.common.abstraction;

import eu.highgeek.common.objects.ChatChannel;
import eu.highgeek.common.objects.PlayerSettings;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface CommonPlayer {

    List<ChatChannel> getListeningChannels();
    ChatChannel getChannelOut();
    UUID getUniqueId();
    String getPlayerName();
    PlayerSettings getPlayerSettings();
    PlayerSettings getPlayerSettingsFromRedis();


    void joinToChannel(ChatChannel channel);
    void disconnectFromChannel(ChatChannel channel);
    void setChannelOut(ChatChannel channel);

}
