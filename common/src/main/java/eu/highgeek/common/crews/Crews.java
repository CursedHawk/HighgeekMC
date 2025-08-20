package eu.highgeek.common.crews;

import com.google.gson.Gson;
import eu.highgeek.common.CommonMain;
import eu.highgeek.common.redis.RedisManager;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Crews {

    @Getter
    HashMap<UUID, Crew> crews = new HashMap<>();

    private final Gson gson = new Gson();
    private final RedisManager redisManager = CommonMain.getRedisManager();

    public Crews(){
        initCrews();
    }

    public void initCrews(){
        List<String> keys = redisManager.getKeysPrefix("crews:*").stream().toList();
        for (String key : keys){
            Crew crew = gson.fromJson(redisManager.getStringRedis(key), Crew.class);
            crews.put(crew.getUuid(), crew);
        }
    }
}
