/* (C)2025 */
package top.archiem.jmp.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.Commands;
import top.archiem.jmp.JMP;

public class MainCommand {
  public static LiteralCommandNode JmpCommand() {
      JMP jmp = JMP.getPlugin(JMP.class);
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
                                    boolean success = jmp.refreshConfig();
                                    if(success){
                                        jmp.log.info("Config Reloaded Successfully");
                                    }
                                  return 1;
                                })))
            .build();
  }
}
