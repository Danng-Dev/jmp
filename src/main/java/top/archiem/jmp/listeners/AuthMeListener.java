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
import top.archiem.jmp.util.TitleHandler;

public class AuthMeListener implements Listener {

    JMP jmp = JMP.getPlugin(JMP.class);

    private final boolean msgPlayer = jmp.isMsgPlayer();
    private final String leaveMsg = jmp.getLeaveMsg();
    private final String joinMsg = jmp.getJoinMsg();
    private final String silentMsg = jmp.getSilentMsg();
    private final String msgPlayerJoin = jmp.getMsgPlayerJoin();
    private final String msgPlayerLeave = jmp.getMsgPlayerLeave();
    private final String title = jmp.getTitle();
    private final String subtitle = jmp.getSubtitle();
    private final boolean titlesEnabled = jmp.isTitlesEnabled();
    private final boolean titlsOnlyOnFirst = jmp.isTitleOnFirst();
    private final String silentPerm = "JMP.silent";
    
    private final boolean sendRegisterMsg = jmp.isSendregistermsg();
    private final String registerMessage = jmp.getRegisterMessage();

    FileConfiguration config = jmp.getConfig();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(LoginEvent event){
        try {
            Player player = event.getPlayer();
            TitleHandler titles = new TitleHandler(player);
            TextFormat tf = new TextFormat(jmp.papienabled);
            if (msgPlayer) {
                player.sendMessage(tf.format(msgPlayerJoin, player));
            }
            if(!titlsOnlyOnFirst && titlesEnabled && !player.hasPermission(silentPerm)){
                player.showTitle(titles.titleBuilder(title, subtitle, null));
            }
            if (player.hasPermission(silentPerm)) {
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
            TitleHandler titles = new TitleHandler(player);
            TextFormat tf = new TextFormat(jmp.papienabled);
            if (msgPlayer) {
                player.sendMessage(tf.format(msgPlayerJoin, player));
            }
            if(titlesEnabled && !player.hasPermission(silentPerm)){
                player.showTitle(titles.titleBuilder(title, subtitle, null));
            }
            if (player.hasPermission(silentPerm)) {
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
