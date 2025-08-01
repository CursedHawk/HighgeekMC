package eu.highgeek.paper.managers;

import eu.highgeek.common.CommonMain;
import eu.highgeek.paper.PaperMain;
import eu.highgeek.paper.integration.authme.AuthMeListener;
import eu.highgeek.paper.listeners.ChatListener;
import eu.highgeek.paper.listeners.ConnectionListener;
import eu.highgeek.paper.listeners.StatsListener;

public class EventManager {

    private final PaperMain paperMain;

    public EventManager(PaperMain paperMain){
        this.paperMain = paperMain;
        this.registerEvents();
    }

    public void registerEvents(){
        paperMain.getServer().getPluginManager().registerEvents(new ConnectionListener(), paperMain);
        paperMain.getServer().getPluginManager().registerEvents(new ChatListener(), paperMain);
        paperMain.getServer().getPluginManager().registerEvents(new StatsListener(CommonMain.getRedisManager()), paperMain);
        if(paperMain.getServer().getPluginManager().getPlugin("AuthMe") != null){
            AuthMeListener listener = new AuthMeListener();
            paperMain.getServer().getPluginManager().registerEvents(listener, paperMain);
            PaperMain.setAuthMeListener(listener);
            PaperMain.setAuthme(true);
        }
    }
}
