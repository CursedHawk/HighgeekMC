package eu.highgeek.common.economy;

import eu.highgeek.common.CommonMain;
import eu.highgeek.common.objects.RedisEconomyResponse;
import eu.highgeek.common.redis.RedisManager;
import lombok.Getter;

@Getter
public class Currency {

    private final String id;
    private final String name;
    private final String symbol;
    private final String color;
    private final String defaultValue;
    private final boolean isVault;


    public Currency(String id, String name, String symbol, String color, String defaultValue, boolean isVaultCurrency){
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.color = color;
        this.defaultValue = defaultValue;
        this.isVault = isVaultCurrency;
    }

    public RedisEconomyResponse depositAccount(String account, double val){
        CommonMain.getLogger().debug("depositAccount");
        return CommonMain.getRedisManager().depositAccountBalance(account, id, val);
    }

    public RedisEconomyResponse withdrawAccount(String account, double val){
        CommonMain.getLogger().debug("withdrawAccount");
        return CommonMain.getRedisManager().withdrawAccountBalance(account, id, val);
    }

    public double getAccountCurrency(String account){
        return CommonMain.getRedisManager().getAccountBalance(account, id);
    }

    public void setAccountCurrency(String account, double val){
        CommonMain.getLogger().debug("setAccountCurrency");
        CommonMain.getRedisManager().setAccountBalance(account, id, val);
    }

    public boolean accountHas(String account, double val){
        CommonMain.getLogger().debug("accountHas");
        return CommonMain.getRedisManager().hasAccountBalance(account, id, val);
    }

    public boolean accountExists(String account){
        CommonMain.getLogger().debug("accountExists");
        return CommonMain.getRedisManager().accountExists(account, id);
    }
}
