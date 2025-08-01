package eu.highgeek.paper.features.economy;

import eu.highgeek.common.CommonMain;
import eu.highgeek.common.objects.RedisEconomyResponse;
import eu.highgeek.common.redis.RedisManager;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

import org.bukkit.OfflinePlayer;

import java.util.List;

public class VaultEconomyImpl implements Economy {

    @Getter
    private final Currency currency;

    private final RedisManager redisManager = CommonMain.getRedisManager();

    public VaultEconomyImpl(Currency currency){
        this.currency = currency;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "HG" + currency.getName();
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return "";
    }

    @Override
    public String currencyNamePlural() {
        return currency.getName();
    }

    @Override
    public String currencyNameSingular() {
        return currency.getName();
    }

    @Override
    public boolean hasAccount(String s) {
        return redisManager.playerExists(s);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return hasAccount(offlinePlayer.getName());
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return redisManager.playerExists(s);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return hasAccount(offlinePlayer.getName());
    }

    @Override
    public double getBalance(String playerName) {
        return redisManager.getPlayerBalance(playerName, currency.getId());
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return getBalance(offlinePlayer.getName());
    }

    @Override
    public double getBalance(String playerName, String s1) {
        return redisManager.getPlayerBalance(playerName, currency.getId());
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return getBalance(offlinePlayer.getName());
    }

    @Override
    public boolean has(String playerName, double v) {
        return redisManager.hasPlayerBalance(playerName, currency.getId(), v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return has(offlinePlayer.getName(), v);
    }

    @Override
    public boolean has(String playerName, String s1, double v) {
        return redisManager.hasPlayerBalance(playerName, currency.getId(), v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return has(offlinePlayer.getName(), v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double v) {
        return redisToEconomyResponse(redisManager.withdrawPlayerBalance(playerName, currency.getId(), v));
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        return withdrawPlayer(offlinePlayer.getName(), v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String s1, double v) {
        return redisToEconomyResponse(redisManager.withdrawPlayerBalance(playerName, currency.getId(), v));
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return withdrawPlayer(offlinePlayer.getName(), v);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double v) {
        return redisToEconomyResponse(redisManager.depositPlayerBalance(playerName, currency.getId(), v));
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        return depositPlayer(offlinePlayer.getName(), v);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String s1, double v) {
        return depositPlayer(playerName, v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return depositPlayer(offlinePlayer.getName(), v);
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return notImplemented();
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return notImplemented();
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return notImplemented();
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return notImplemented();
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return notImplemented();
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return notImplemented();
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return notImplemented();
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return notImplemented();
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return notImplemented();
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return notImplemented();
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return notImplemented();
    }

    @Override
    public List<String> getBanks() {
        return List.of();
    }

    //TODO What with this?
    @Override
    public boolean createPlayerAccount(String s) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    public static EconomyResponse redisToEconomyResponse(RedisEconomyResponse redisEconomyResponse){
        if (redisEconomyResponse.success()) {
            return new EconomyResponse(redisEconomyResponse.ammount(), redisEconomyResponse.balance(), ResponseType.SUCCESS, null);
        }else{
            return new EconomyResponse(redisEconomyResponse.ammount(), redisEconomyResponse.balance(), ResponseType.FAILURE, "Insufficient funds");
        }
    }

    public static EconomyResponse notImplemented(){
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, null);
    }
}
