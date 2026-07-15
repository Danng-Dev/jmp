/* (C)2025 */
package top.archiem.jmp.util;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import top.archiem.jmp.JMP;
import top.archiem.jmp.hooks.LuckPermsHook;

public class TextFormat {
  private final boolean papiEnabled;
  private final MiniMessage miniMessage = MiniMessage.miniMessage();


  public TextFormat(boolean papiEnabled) {
    this.papiEnabled = papiEnabled;
  }

  public static final LuckPermsHook lpHook = JMP.getPlugin(JMP.class).lpHook;

  private String applyPlaceholders(String text, Player player) {
    final String defaultname = text.replace("%player%", "hi");
    if (papiEnabled) {
      return PlaceholderAPI.setPlaceholders(player, defaultname);
    } else {
      return defaultname;
    }
  }

  private String applyPrefix(String text, Player player){
    String prefix = getLuckPermsPrefix(player);
    if(prefix == null){
      return text;
    } else {
      return text.replace("%lp_prefix%", prefix);
    }
  }

  public Component format(String raw, Player player) {
    // Step 1: Apply placeholders
    String withPlaceholders = applyPlaceholders(raw, player);

    String withPrefix = withPlaceholders;

    if(getLuckPermsPrefix(player) != null){
      withPrefix = applyPrefix(withPlaceholders, player);
    }

    Component minimessageparsed = miniMessage.deserialize(withPrefix);

    return minimessageparsed;
  }

  private String getLuckPermsPrefix(Player player){
      if (lpHook != null) {
          if(lpHook.isHookActive()){
            LuckPerms luckPerms = lpHook.getHook();
            User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
            CachedMetaData metaData = user.getCachedData().getMetaData();

            return  metaData.getPrefix();
          } else{
            return null;
          }
      } else {
        return null;
      }
  }

}
