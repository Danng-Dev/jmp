/* (C)2025 */
package top.archiem.jmp;

import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

import org.bstats.bukkit.Metrics;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.error.YAMLException;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

import top.archiem.jmp.commands.MainCommand;
import top.archiem.jmp.hooks.AuthMeHook;
import top.archiem.jmp.listeners.AuthMeListener;
import top.archiem.jmp.util.TextFormat;
import top.archiem.jmp.util.modrinth.Modrinth;

public class JMP extends JavaPlugin implements Listener {

  FileConfiguration config = getConfig();

  PluginManager pluginManager = this.getServer().getPluginManager();

  public Logger log = this.getLogger();

  boolean errorConfig = false;

  public boolean papienabled = false;
  private boolean authenabled = false;

  private String leaveMsg;
  private String joinMsg;
  private String msgPlayerJoin;
  private String msgPlayerLeave;
  private String silentMsg;
  private String firstJoinMsg;
  private String firstPlayerMsg;
  private boolean msgPlayer;
  private boolean firstMsgPlayer;
  private boolean msgFirstJoin;


  private boolean amlock = false;
  private boolean amenabled = false;
  private String lockmsg;
  private String registerMessage;
  private boolean sendregistermsg;

  private int bstatsId = 22405;

  private Listener authMeListener;
  private AuthMeHook authMeHook;
  //Easier than double-checking
  private boolean lockjmsg;

  //Version Checking, change with each version!
  private String version = "4.0.0";

  @Override
  public void onEnable() {
    this.log.info("Join Message plus enabling");
    Modrinth.initialize(this);
    this.log.info("\n" +
            "   $$$$$\\ $$\\      $$\\ $$$$$$$\\  \n" +
            "   \\__$$ |$$$\\    $$$ |$$  __$$\\ \n" +
            "      $$ |$$$$\\  $$$$ |$$ |  $$ |\n" +
            "      $$ |$$\\$$\\$$ $$ |$$$$$$$  |\n" +
            "$$\\   $$ |$$ \\$$$  $$ |$$  ____/ \n" +
            "$$ |  $$ |$$ |\\$  /$$ |$$ |      \n" +
            "\\$$$$$$  |$$ | \\_/ $$ |$$ |            Paper version:\n" +
            " \\______/ \\__|     \\__|\\__|          "+version+"\n" +
            "                                 ");
    log.info("Detecting Dependencies");
    papienabled = checkDependency("PlaceholderAPI");
    if (papienabled) {
      this.log.info("You have Placeholder API enabled, you can now use placeholders.");
    } else {
      this.log.warning(
          "You do not have Placeholder API enabled, the plugin will still work, but you cannot use"
              + " placeholders.");
    }
    authenabled = checkDependency("AuthMe");
    if(authenabled){
      log.info("Auth Me Is installed, authme plugin config can now be used");
      authMeHook = new AuthMeHook();
    } else {
      log.info("AuthMe not detected");
    }
    this.log.info("Loading Config");
    saveDefaultConfig();
    try {
      leaveMsg = config.getString("main.messages.leave");
      joinMsg = config.getString("main.messages.join");

      msgPlayerJoin = config.getString("main.player-messages.join");
      msgPlayerLeave = config.getString("main.player-messages.leave");
      silentMsg = config.getString("main.player-messages.silent");
      firstPlayerMsg = config.getString("main.player-messages.first");

      msgPlayer = config.getBoolean("main.send-message-to-player");

      firstJoinMsg = config.getString("main.messages.first");
      msgFirstJoin = config.getBoolean("main.enable-first-join-message");
      firstMsgPlayer = config.getBoolean("main.send-first-join-message-to-player");

      amenabled = config.getBoolean("authme.enabled");
      amlock = config.getBoolean("authme.delay-join-messages-until-login");
      lockmsg = config.getString("authme.login-required-message");

    } catch (YAMLException e) {
      log.severe("The config has been incorrectly configured");
      log.severe(e.getMessage());
      pluginManager.disablePlugin(this);
    }
    this.log.info("Loaded config");
    if (!amenabled &&  authenabled){log.warning("AuthMe is installed, but the config is disabled.");}
    this.log.info("Loading listener");
    pluginManager.registerEvents(this, this);
    if(amenabled && authenabled){ registerAuthMeComponents(); }
    this.log.info("Registered Listener");
    this.log.info("Loading bstats");
    Metrics metrics = new Metrics(this, bstatsId);
    this.log.info("Loaded bstats");
    this.log.info("Loading commands");
    this.getLifecycleManager()
        .registerEventHandler(
            LifecycleEvents.COMMANDS,
            commands -> {
              commands.registrar().register(MainCommand.JmpCommand());
            });
    if (!authenabled){ lockjmsg = false;} else if(!amenabled){lockjmsg = false;} else {lockjmsg = true;}
  }


  public void registerAuthMeComponents() {
    log.info("Hooking into AuthMe");
    authMeHook.initializeAuthMeHook();
    if (authMeListener == null) {
      authMeListener = new AuthMeListener();
      pluginManager.registerEvents(authMeListener, this);
    }
  }

  public void removeAuthMeHook() {
    log.info("Unhooking from AuthMe");
    authMeHook.removeAuthMeHook();
  }

  @Override
  public void onDisable() {
    this.log.info("Disabling...");
    if(authenabled){
      removeAuthMeHook();
    }
    if (this.errorConfig) {
      this.log.warning("This plugin is shutting down due to an error with the config");
    }
  }

  @EventHandler
  public void playerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    TextFormat tf = new TextFormat(papienabled);
      if (!lockjmsg) {

          if (!player.hasPlayedBefore()) {
            if(msgFirstJoin) {
              event.joinMessage(tf.format(firstJoinMsg, player));
            }
            if(firstMsgPlayer){
              player.sendMessage(tf.format(firstPlayerMsg, player));
            }
          } else  {
            if (msgPlayer) {
              player.sendMessage(tf.format(msgPlayerJoin, player));
            }
            if (player.hasPermission("JMP.silent")) {
              event.joinMessage(null);
              player.sendMessage(tf.format(silentMsg, player));
            } else if (player.hasPermission("JMP.premium.1")) {
              event.joinMessage(tf.format(config.getString("main.premium-messages.1"), player));
            } else if (player.hasPermission("JMP.premium.2")) {
              event.joinMessage(tf.format(config.getString("main.premium-messages.2"), player));
            } else if (player.hasPermission("JMP.premium.3")) {
              event.joinMessage(tf.format(config.getString("main.premium-messages.3"), player));
            } else if (player.hasPermission("JMP.premium.4")) {
              event.joinMessage(tf.format(config.getString("main.premium-messages.4"), player));
            } else {
              event.joinMessage(tf.format(joinMsg, player));
            }
          }
      } else {
        player.sendMessage(tf.format(lockmsg, player));
        event.joinMessage(null);
      }
  }

  @EventHandler
  public void playerLeave(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    TextFormat tf = new TextFormat(papienabled);
      if (!lockjmsg) {
          if (player.hasPermission("JMP.silent")) {
            event.quitMessage(null);
          } else {
            event.quitMessage(tf.format(leaveMsg, player));
            if (msgPlayer) player.sendMessage(tf.format(msgPlayerLeave, player));
          }
      } else {
        player.sendMessage(tf.format(lockmsg, player));
        event.quitMessage(null);
      }
  }

  public boolean checkDependency(String pluginID){
      try {
          return Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(pluginID)).isEnabled();
      } catch (NullPointerException e) {
          return false;
      }
  }

  public boolean refreshConfig() {
    try {
      reloadConfig();
      saveDefaultConfig();
      leaveMsg = config.getString("main.messages.leave");
      joinMsg = config.getString("main.messages.join");
      msgPlayerJoin = config.getString("main.messageplayer.join");
      msgPlayerLeave = config.getString("main.messageplayer.leave");
      silentMsg = config.getString("main.messageplayer.silent");
      msgPlayer = config.getBoolean("main.sendmessagetoplayer");
    } catch (YAMLException e) {
      log.severe("The config has been incorrectly configured");
      log.severe(e.getMessage());
      return false;
    } catch (Exception e) {
      log.fine("An unknown error occurred: " + e.getMessage());
      log.fine(
          "Please open an issue on our github"
              + " page(https://github.com/Danng-Dev/JoinMessagePluss/issues) with the provided"
              + " stack trace");
      log.fine(Arrays.toString(e.getStackTrace()));
      return false;
    }
    return true;
  }

  public boolean isMsgPlayer() {
    return msgPlayer;
  }

  public String getSilentMsg() {
    return silentMsg;
  }

  public String getMsgPlayerLeave() {
    return msgPlayerLeave;
  }

  public String getMsgPlayerJoin() {
    return msgPlayerJoin;
  }

  public String getJoinMsg() {
    return joinMsg;
  }

  public String getLeaveMsg() {
    return leaveMsg;
  }

  public String getRegisterMessage() {
    return registerMessage;
  }

  public boolean isSendregistermsg() {
    return sendregistermsg;
  }

}
