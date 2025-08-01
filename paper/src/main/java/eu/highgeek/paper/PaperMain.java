package eu.highgeek.paper;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import eu.highgeek.common.CommonMain;
import eu.highgeek.common.abstraction.CommonLogger;
import eu.highgeek.common.abstraction.CommonPlugin;
import eu.highgeek.common.events.EventBus;
import eu.highgeek.common.events.RedisEventBus;
import eu.highgeek.paper.features.chat.ChannelCommand;
import eu.highgeek.paper.features.chat.ChannelMenu;
import eu.highgeek.paper.impl.PaperLogger;
import eu.highgeek.paper.impl.PaperEventBus;
import eu.highgeek.paper.integration.authme.AuthMeListener;
import eu.highgeek.paper.listeners.RedisEventListener;
import eu.highgeek.paper.managers.EventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public class PaperMain extends JavaPlugin implements CommonPlugin {

    @Getter
    private static PaperMain mainInstance;
    @Getter
    private static EventManager eventManager;
    @Getter
    private static ProtocolManager protocolManager;
    @Getter
    private static final HashMap<String, ChannelMenu> openedChannelMenus = new HashMap<>();
    @Getter
    @Setter
    private static AuthMeListener authMeListener;
    @Getter
    @Setter
    private static boolean authme = false;

    @Getter
    private static Economy economy = null;
    @Getter
    private static Chat chat = null;


    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable(){
        logger = new PaperLogger(this.getServer().getLogger());
        eventBus = new PaperEventBus();
        redisEventBus = new RedisEventListener();
        commonMain = new CommonMain(this);
        mainInstance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        eventManager = new EventManager(this);

        registerCommands();
    }

    @Override
    public void onDisable(){
        CommonMain.getRedisManager().stopListener();
        CommonMain.getRabbitManager().stopListener();
    }

    private void registerCommands(){
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(ChannelCommand.createCommand().build());
        });
    }

    private CommonMain commonMain;
    @Override
    public CommonMain getCommon() {
        return commonMain;
    }
    private PaperLogger logger;
    @Override
    public CommonLogger getCommonLogger() {
        return logger;
    }
    private EventBus eventBus;
    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    private RedisEventBus redisEventBus;
    @Override
    public RedisEventBus getRedisEventBus() {
        return redisEventBus;
    }

    private File dataDirectory;
    @Override
    public File getFile() {
        return dataDirectory;
    }

    //TODO chat
    private boolean setupChat(){
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    //TODO economy
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }


}