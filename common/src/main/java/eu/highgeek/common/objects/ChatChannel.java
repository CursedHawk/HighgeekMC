package eu.highgeek.common.objects;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.List;


@Getter
@Setter
@Builder
public class ChatChannel {
    public String name;
    public String fancyName;
    public String prefix;
    public boolean isLocal;
    public boolean isDefault;
    @Nullable
    public String permission;
    @Nullable
    public String speakPermission;
}
