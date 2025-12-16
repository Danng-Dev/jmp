/* (C)2025 */
package top.archiem.jmp.util;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class TextFormat {
  private final boolean papiEnabled;
  private final MiniMessage miniMessage = MiniMessage.miniMessage();
  private final LegacyComponentSerializer legacy = LegacyComponentSerializer.legacyAmpersand();

  public TextFormat(boolean papiEnabled) {
    this.papiEnabled = papiEnabled;
  }

  private String applyPlaceholders(String text, Player player) {
    final String defaultname = text.replace("%player%", player.getName());
    if (papiEnabled) {
      return PlaceholderAPI.setPlaceholders(player, defaultname);
    } else {
      return defaultname;
    }
  }

  public Component format(String raw, Player player) {
    // Step 1: Apply placeholders
    String withPlaceholders = applyPlaceholders(raw, player);

    Component minimessageparsed = miniMessage.deserialize(withPlaceholders);

    return minimessageparsed;
  }
}
