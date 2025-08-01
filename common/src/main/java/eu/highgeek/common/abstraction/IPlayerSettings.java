package eu.highgeek.common.abstraction;

import eu.highgeek.common.objects.PlayerSettings;

public interface IPlayerSettings {

    PlayerSettings getPlayerSettingsFromRedis();

    void setPlayerSettingsInRedis(PlayerSettings playerSettings);
}
