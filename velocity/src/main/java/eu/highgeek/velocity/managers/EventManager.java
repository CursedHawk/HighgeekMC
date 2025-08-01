package eu.highgeek.velocity.managers;

import com.velocitypowered.api.proxy.ProxyServer;
import eu.highgeek.velocity.VelocityMain;
import eu.highgeek.velocity.features.playersender.Sender;
import eu.highgeek.velocity.listeners.ConnectionListener;

public class EventManager {

    private final VelocityMain main;

    public EventManager(VelocityMain main){
        this.main = main;
        registerEvents();
    }

    private void registerEvents(){
        VelocityMain.getProxyServer().getEventManager().register(main, new ConnectionListener());
        VelocityMain.getProxyServer().getEventManager().register(main, new Sender());
    }
}
