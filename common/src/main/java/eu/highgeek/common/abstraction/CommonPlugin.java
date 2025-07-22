package eu.highgeek.common.abstraction;

import eu.highgeek.common.CommonMain;
import eu.highgeek.common.events.EventBus;
import eu.highgeek.common.events.RedisEventBus;

import java.io.File;

public interface CommonPlugin {
    CommonMain getCommon();
    File getDataFolder();
    CommonLogger getCommonLogger();
    EventBus getEventBus();
    RedisEventBus getRedisEventBus();
}
