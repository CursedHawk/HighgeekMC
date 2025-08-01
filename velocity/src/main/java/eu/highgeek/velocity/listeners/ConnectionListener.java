package eu.highgeek.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import eu.highgeek.common.CommonMain;
import eu.highgeek.common.abstraction.IPlayerSettings;
import eu.highgeek.common.objects.PlayerSettings;
import eu.highgeek.velocity.impl.VelocityPlayer;

public class ConnectionListener {


    @Subscribe
    public void onPlayerJoin(LoginEvent event){
        //CommonMain.getCommonPlayers().add(new VelocityPlayer(event.getPlayer()));
    }

}
