package eu.highgeek.paper.listeners;

import eu.highgeek.common.CommonMain;
import eu.highgeek.common.config.ConfigManager;
import eu.highgeek.common.redis.RedisManager;
import eu.highgeek.paper.PaperMain;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.json.Path2;

import java.util.Objects;


public class StatsListener implements Listener {
    public static final String servername = ConfigManager.getPluginConfig().getString("server-name");
//TODO Rewrite na rabbit s c# backendem
    private final RedisManager redisManager;
    public StatsListener(RedisManager redisManager){
        this.redisManager = redisManager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onStatsIncrement(PlayerStatisticIncrementEvent event){
        Bukkit.getScheduler().runTaskAsynchronously(PaperMain.getMainInstance(), () -> runAsync(event));
    }

    private void runAsync(PlayerStatisticIncrementEvent event){
        try {
            switch (event.getStatistic().getType()){
                case BLOCK, ITEM:{
                    try {
                        if(!Objects.equals(event.getMaterial(), null)){
                            redisManager.jsonSet("players:stats:" + event.getPlayer().getName()+":"+servername, event.getStatistic().name() + "." + event.getMaterial(), String.valueOf(event.getNewValue()));
                        }
                    }catch (JedisDataException exception){
                        try {
                            redisManager.jsonSet("players:stats:" + event.getPlayer().getName()+":"+servername, event.getStatistic().name(), "{\""+ event.getMaterial() +"\": \"" + String.valueOf(event.getNewValue()) +"\"}");
                        }catch (JedisDataException e){
                            CommonMain.getLogger().debug("Statistics: really bad happens");
                        }
                    }
                }
                case ENTITY:{
                    try {
                        if(!Objects.equals(event.getEntityType(), null)){
                            redisManager.jsonSet("players:stats:" + event.getPlayer().getName()+":"+servername, event.getStatistic().name() + "." + event.getEntityType(), String.valueOf(event.getNewValue()));
                        }
                    }catch (JedisDataException exception){
                        try {
                            redisManager.jsonSet("players:stats:" + event.getPlayer().getName()+":"+servername, event.getStatistic().name(), "{\""+ event.getEntityType() +"\": \"" + String.valueOf(event.getNewValue()) +"\"}");
                        }catch (JedisDataException e){
                            CommonMain.getLogger().debug("Statistics: really bad happens");
                        }
                    }
                }
                case UNTYPED:{
                    try {
                        redisManager.jsonSet("players:stats:" + event.getPlayer().getName()+":"+servername, "GENERAL." + event.getStatistic().name(), String.valueOf(event.getNewValue()));
                    }catch (JedisDataException exception){
                        try {
                            redisManager.jsonSet("players:stats:" + event.getPlayer().getName()+":"+servername, "GENERAL", "{\""+ event.getStatistic().name() +"\": \"" + String.valueOf(event.getNewValue()) +"\"}");
                        }catch (JedisDataException e){
                            CommonMain.getLogger().debug("Statistics: really bad happens");
                        }
                    }
                }
            }
        }catch (JedisDataException exception){
            CommonMain.getLogger().debug("Statistics: really bad happens");
            exception.printStackTrace();
        }
    }

    public static void createNewStatsCounter(String playerName) throws JedisDataException{
        CommonMain.getLogger().debug("Creating new stat counter created for player " + playerName);
        CommonMain.getRedisManager().jsonSet("players:stats:" + playerName+":"+servername, "{\n" +
                "    \"DROP\": {},\n" +
                "    \"PICKUP\": {},\n" +
                "    \"MINE_BLOCK\": {},\n" +
                "    \"USE_ITEM\": {},\n" +
                "    \"BREAK_ITEM\": {},\n" +
                "    \"CRAFT_ITEM\": {},\n" +
                "    \"KILL_ENTITY\": {},\n" +
                "    \"ENTITY_KILLED_BY\": {},\n" +
                "    \"GENERAL\": {}\n" +
                "}");
        CommonMain.getLogger().debug("New stat counter created for player " + playerName);
    }
}

