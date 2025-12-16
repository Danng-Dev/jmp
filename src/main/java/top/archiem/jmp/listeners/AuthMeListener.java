package top.archiem.jmp.listeners;

import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.RegisterEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import top.archiem.jmp.JMP;
import top.archiem.jmp.util.TextFormat;

public class AuthMeListener implements Listener {

    JMP jmp = JMP.getPlugin(JMP.class);

    private boolean msgPlayer = jmp.isMsgPlayer();
    private String leaveMsg = jmp.getLeaveMsg();
    private String joinMsg = jmp.getJoinMsg();
    private String silentMsg = jmp.getSilentMsg();
    private String msgPlayerJoin = jmp.getMsgPlayerJoin();
    private String msgPlayerLeave = jmp.getMsgPlayerLeave();
    
    private boolean sendRegisterMsg = jmp.isSendregistermsg();
    private String registerMessage = jmp.getRegisterMessage();

    FileConfiguration config = jmp.getConfig();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(LoginEvent event){
        try {
            Player player = event.getPlayer();
            TextFormat tf = new TextFormat(jmp.papienabled);
            if (msgPlayer) {
                player.sendMessage(tf.format(msgPlayerJoin, player));
            }
            if (player.hasPermission("JMP.silent")) {
                jmp.getServer().sendMessage(null);
                player.sendMessage(tf.format(silentMsg, player));
            } else if (player.hasPermission("JMP.premium.1")) {
                jmp.getServer().sendMessage(tf.format(config.getString("main.premium.1"), player));
            } else if (player.hasPermission("JMP.premium.2")) {
                jmp.getServer().sendMessage(tf.format(config.getString("main.premium.2"), player));
            } else if (player.hasPermission("JMP.premium.3")) {
                jmp.getServer().sendMessage(tf.format(config.getString("main.premium.3"), player));
            } else if (player.hasPermission("JMP.premium.4")) {
                jmp.getServer().sendMessage(tf.format(config.getString("main.premium.4"), player));
            } else {
                jmp.getServer().sendMessage(tf.format(joinMsg, player));
            }
        } catch (Exception e) {
            jmp.log.severe(e.getStackTrace().toString() + "\n Please file an issue for this error on the github page");
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRegister(RegisterEvent event){
        try {
            Player player = event.getPlayer();
            TextFormat tf = new TextFormat(jmp.papienabled);
            if (msgPlayer) {
                player.sendMessage(tf.format(msgPlayerJoin, player));
            }
            if (player.hasPermission("JMP.silent")) {
                jmp.getServer().sendMessage(null);
                player.sendMessage(tf.format(silentMsg, player));
            } else if (player.hasPermission("JMP.premium.1")) {
                jmp.getServer().sendMessage(tf.format(config.getString("main.premium-messages.1"), player));
            } else if (player.hasPermission("JMP.premium.2")) {
                jmp.getServer().sendMessage(tf.format(config.getString("main.premium-messages.2"), player));
            } else if (player.hasPermission("JMP.premium.3")) {
                jmp.getServer().sendMessage(tf.format(config.getString("main.premium-messages.3"), player));
            } else if (player.hasPermission("JMP.premium.4")) {
                jmp.getServer().sendMessage(tf.format(config.getString("main.premium-messages.4"), player));
            } else {
                jmp.getServer().sendMessage(tf.format(joinMsg, player));
            }
        } catch (Exception e) {
            jmp.log.severe(e.getStackTrace().toString() + "\n Please file an issue for this error on the github page");
        }
    }

}
