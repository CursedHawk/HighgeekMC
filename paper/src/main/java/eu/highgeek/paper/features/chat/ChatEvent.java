package eu.highgeek.paper.features.chat;

import eu.highgeek.common.CommonMain;
import eu.highgeek.common.objects.ChatChannel;
import eu.highgeek.common.objects.ChatMessage;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ChatEvent extends Event {

    public ChatEvent(ChatMessage message, boolean isAsync){
        super(isAsync);
        this.message = message;
        this.channel = CommonMain.getChannelManager().getChatChannelFromName(message.getChannel());
    }

    @Getter
    private ChatMessage message;

    @Getter
    private ChatChannel channel;




    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
