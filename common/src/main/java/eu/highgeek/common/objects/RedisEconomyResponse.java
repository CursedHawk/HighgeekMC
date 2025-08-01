package eu.highgeek.common.objects;

public record RedisEconomyResponse(boolean success, double balance, double ammount){
    public RedisEconomyResponse(boolean success, double balance, double ammount)
{

    this.success = success;
    this.balance = balance;
    this.ammount = ammount;
}
}