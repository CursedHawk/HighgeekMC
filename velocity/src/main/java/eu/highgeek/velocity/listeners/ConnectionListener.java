package eu.highgeek.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;

public class ConnectionListener {


    @Subscribe
    public void onPlayerJoin(LoginEvent event){

    }

    @Subscribe
    public void onPlayerLeave(DisconnectEvent event){

    }

}
