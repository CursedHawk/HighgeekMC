package eu.highgeek.common.crews;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Crew {

    private final String name;
    private final UUID uuid;
    private final String owner;
    private final List<String> members;
    private final double balance;

    public Crew(String name, UUID uuid, String owner, List<String> members, double balance){
        this.name = name;
        this.uuid = uuid;
        this.owner = owner;
        this.members = members;
        this.balance = balance;
    }
}
