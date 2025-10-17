/* (C)2025 */
package top.archiem.jmp;

import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.plugin.java.JavaPlugin;

public class MainCommand {
  protected static LiteralCommandNode JmpCommand() {
    return
        (
                Commands.literal("jmp")
                    .then(
                        (
                                Commands.literal("reload")
                                    .requires(
                                        sender -> sender.getSender().hasPermission("JMP.cmd")))
                            .executes(
                                ctx -> {
                                    JavaPlugin.getPlugin(JMP.class).refreshConfig();
                                  return 1;
                                })))
            .build();
  }
}
