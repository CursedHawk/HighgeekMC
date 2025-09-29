package eu.highgeek.common.abstraction;

import java.util.UUID;

public interface CommonPlayer {

    UUID getUniqueId();
    String getPlayerName();
    boolean isLogged();
    void setLogged(boolean logged);
    boolean checkPermission(String perm);
}
