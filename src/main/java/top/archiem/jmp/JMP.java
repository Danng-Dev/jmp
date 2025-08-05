package top.archiem.jmp;


import java.io.IOException;
import java.util.logging.Logger;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bstats.bukkit.Metrics;
import org.yaml.snakeyaml.error.YAMLException;

public final class JMP extends JavaPlugin implements Listener {

    FileConfiguration config = getConfig();

    Logger log = getLogger();


    boolean errorConfig = false;

    public boolean papienabled = false;

    public String leaveMsg;
    public String joinMsg ;
    public String msgPlayerJoin;
    public String msgPlayerLeave;
    public String silentMsg ;
    public boolean msgPlayer;

    @Override
    public void onEnable() {
        this.log.info("Join Message plus enabling");
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.log.info("You have Placeholder API enabled, you can now use placeholders.");
            this.papienabled = true;
        } else {
            this.log.warning("You do not have Placeholder API enabled, the plugin will still work, but you cannot use placeholders.");
        }
        this.log.info("Loading Config");
        saveDefaultConfig();
        try {
            leaveMsg = config.getString("main.messages.leave");
            joinMsg = config.getString("main.messages.join");
            msgPlayerJoin = config.getString("main.messageplayer.join");
            msgPlayerLeave= config.getString("main.messageplayer.leave");
            silentMsg = config.getString("main.messageplayer.silent");
            msgPlayer = config.getBoolean("main.sendmessagetoplayer");
        } catch (YAMLException e){
            log.severe("The config has been incorrectly configured");
            log.severe(e.getMessage());
            this.getServer().getPluginManager().disablePlugin(this);
        }
        this.log.info("Loaded config");
        this.log.info("Loading listener");
        getServer().getPluginManager().registerEvents(this, this);
        this.log.info("Registered Listener");
        this.log.info("Loading bstats");
        int ID = 22405;
        Metrics metrics = new Metrics(this, ID);
        this.log.info("Loaded bstats");
        this.log.info("Loading commands");
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(MainCommand.MainCommand());
        });

    }

    @Override
    public void onDisable() {
        this.log.info("Disabling...");
        if (this.errorConfig){
            this.log.warning("This plugin is shutting down due to an error with the config");
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        textFormat tf = new textFormat(papienabled);
        if(msgPlayer){
            player.sendMessage(tf.format(msgPlayerJoin, player));
        }
        if(player.hasPermission("JMP.silent")){
            event.joinMessage(null);
            player.sendMessage(tf.format(silentMsg, player));
        } else if (player.hasPermission("JMP.premium.1")) {
            event.joinMessage(tf.format(config.getString("main.premium.1"), player));
        } else if(player.hasPermission("JMP.premium.2")){
            event.joinMessage(tf.format(config.getString("main.premium.2"), player));
        } else if (player.hasPermission("JMP.premium.3")) {
            event.joinMessage(tf.format(config.getString("main.premium.3"), player));
        } else if (player.hasPermission("JMP.premium.4")) {
            event.joinMessage(tf.format(config.getString("main.premium.4"), player));
        } else {
            event.joinMessage(tf.format(joinMsg , player));
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event) throws IOException {
        Player player = event.getPlayer();
        textFormat tf = new textFormat(papienabled);
        if(player.hasPermission("JMP.silent")){
            event.quitMessage(null);
        } else {
            event.quitMessage(tf.format(leaveMsg, player));
            if (msgPlayer){
                player.sendMessage(tf.format(msgPlayerLeave, player));
            }
        }
    }

    public boolean refreshConfig(){
        try{
            reloadConfig();
            saveDefaultConfig();
            leaveMsg = config.getString("main.messages.leave");
            joinMsg = config.getString("main.messages.join");
            msgPlayerJoin = config.getString("main.messageplayer.join");
            msgPlayerLeave= config.getString("main.messageplayer.leave");
            silentMsg = config.getString("main.messageplayer.silent");
            msgPlayer = config.getBoolean("main.sendmessagetoplayer");
        } catch(YAMLException e){
            log.severe("The config has been incorrectly configured");
            log.severe(e.getMessage());
            return false;
        } catch (Exception e){
            log.fine("An unknown error occurred: " + e.getMessage());
            log.fine("Please open an issue on our github page(https://github.com/Danng-Dev/JoinMessagePluss/issues) with the provided stack trace");
            log.fine(e.getStackTrace().toString());
            return false;
        }
        return true;
    }


}
