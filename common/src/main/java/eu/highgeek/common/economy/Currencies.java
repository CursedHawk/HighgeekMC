package eu.highgeek.common.economy;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.highgeek.common.CommonMain;
import eu.highgeek.common.redis.RedisManager;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

public class Currencies {

    private final Gson gson = new Gson();
    private final RedisManager redisManager = CommonMain.getRedisManager();

    @Getter
    private final HashMap<String, Currency> currencies = new HashMap<>();
    private String vault;


    public Currency getVaultCurrency()
    {
        return currencies.get(vault);
    }

    public Currencies(){
        initCurrencies();
    }

    public void initCurrencies(){
        currencies.clear();
        List<Currency> list = gson.fromJson(redisManager.getStringRedis("settings:common:economy"), new TypeToken<List<Currency>>(){}.getType());
        for (Currency cur : list){
            currencies.put(cur.getId(), cur);
            if(cur.isVault()){
                vault = cur.getId();
            }
        }
    }



}
