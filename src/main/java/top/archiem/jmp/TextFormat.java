/* (C)2025 */
package top.archiem.jmp;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import de.themoep.minedown.adventure.*;

public class TextFormat {
  private final boolean papiEnabled;
  private final MiniMessage miniMessage = MiniMessage.miniMessage();
  private final LegacyComponentSerializer legacy = LegacyComponentSerializer.legacyAmpersand();

  public TextFormat(boolean papiEnabled) {
    this.papiEnabled = papiEnabled;
  }

  private String applyPlaceholders(String text, Player player) {
    if (papiEnabled) {
      return PlaceholderAPI.setPlaceholders(player, text);
    } else {
      return text.replace("%player%", player.getName());
    }
  }

  public Component format(String raw, Player player) {
    // Step 1: Apply placeholders
    String withPlaceholders = applyPlaceholders(raw, player);

    // Step 2: Deserialize legacy '&' formatting to Component
    Component component = legacy.deserialize(withPlaceholders);

    // Step 3: Serialize back into MiniMessage string
    String miniMessageString = MiniMessage.miniMessage().serialize(component);

    Component minedownApply = MineDown.parse(miniMessageString);

    // Step 4: Deserialize using MiniMessage to process tags
    return minedownApply;
  }
}
