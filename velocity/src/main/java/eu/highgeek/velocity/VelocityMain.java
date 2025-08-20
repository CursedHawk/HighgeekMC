package eu.highgeek.velocity;

import com.google.inject.Injector;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import eu.highgeek.common.CommonMain;
import eu.highgeek.common.abstraction.CommonLogger;
import eu.highgeek.common.abstraction.CommonPlugin;
import eu.highgeek.common.events.EventBus;
import eu.highgeek.common.events.RedisEventBus;
import eu.highgeek.velocity.commands.DebugCommand;
import eu.highgeek.velocity.listeners.Listener;
import eu.highgeek.velocity.impl.VelocityLogger;
import eu.highgeek.velocity.impl.VelocityEventBus;
import eu.highgeek.velocity.listeners.RedisEventListener;
import eu.highgeek.velocity.listeners.inputs.ChatListener;
import eu.highgeek.velocity.listeners.inputs.CommandListener;
import eu.highgeek.velocity.listeners.inputs.TabCompleteListener;
import eu.highgeek.velocity.managers.EventManager;
import lombok.Getter;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Path;
import java.util.stream.Stream;

public class VelocityMain implements CommonPlugin {
//https://github.com/RagingTech/NetworkJoinMessages/blob/master/src/main/java/xyz/earthcow/networkjoinmessages/velocity/general/VelocityMain.java
    @Getter
    private static ProxyServer proxyServer;
    @Getter
    private static VelocityMain mainInstance;
    @Getter
    private static EventManager eventManager;

    @Getter
    private CommonMain commonMain;
    @Override
    public CommonMain getCommon() {
        return commonMain;
    }
    private final VelocityLogger logger;
    @Override
    public CommonLogger getCommonLogger() {
        return logger;
    }
    private static EventBus eventBus;
    @Override
    public EventBus getEventBus() {
        return eventBus;
    }
    private static RedisEventBus redisEventBus;
    @Override
    public RedisEventBus getRedisEventBus() {
        return redisEventBus;
    }

    private final File dataDirectory;
    @Override
    public File getDataFolder() {
        return dataDirectory;
    }

    @Inject
    public VelocityMain(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        mainInstance = this;
        this.logger = new VelocityLogger(logger);
        proxyServer = proxy;
        this.dataDirectory = dataDirectory.toFile();
    }
    @Inject
    private Injector injector;

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        eventBus = new VelocityEventBus(this);
        redisEventBus = new RedisEventListener();
        commonMain = new CommonMain(this);
        eventManager = new EventManager(this);


        Stream.of(
                        ChatListener.class,
                        CommandListener.class,
                        TabCompleteListener.class
                ).map(injector::getInstance)
                .forEach(Listener::register);
        registerCommands();
    }

    private void registerCommands(){
        CommandManager commandManager = proxyServer.getCommandManager();

        CommandMeta commandMeta = commandManager.metaBuilder("velocitydebug")
                .plugin(this)
                .build();
        Command debugCommand = new DebugCommand();

        commandManager.register(commandMeta, debugCommand);
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event){
        CommonMain.getRedisManager().stopListener();
        CommonMain.getRabbitManager().stopListener();
    }
}