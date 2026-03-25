package top.archiem.jmp;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class MainCommandTest {

    @Test
    void testMainCommandBuildsCorrectly() {
        var node = MainCommand.MainCommand();
        assertNotNull(node);
        assertEquals("jmp", node.getName());
        assertTrue(node.getChildren().stream().anyMatch(c -> c.getName().equals("reload")));
    }

    @Test
    void testCommandExecutesRefreshConfig() throws Exception {
        // Mock dependencies
        JMP mockPlugin = Mockito.mock(JMP.class);
        Mockito.when(mockPlugin.refreshConfig()).thenReturn(true);
        Mockito.mockStatic(JMP.class).when(() -> JMP.getPlugin(JMP.class)).thenReturn(mockPlugin);

        CommandSender sender = Mockito.mock(CommandSender.class);
        Entity entity = Mockito.mock(Entity.class);
        CommandSourceStack stack = Mockito.mock(CommandSourceStack.class);
        Mockito.when(stack.getSender()).thenReturn(sender);
        Mockito.when(stack.getExecutor()).thenReturn(entity);

        CommandContext<CommandSourceStack> ctx = Mockito.mock(CommandContext.class);
        Mockito.when(ctx.getSource()).thenReturn(stack);

        var node = MainCommand.MainCommand();
        var reload = node.getChild("reload");
        assertNotNull(reload);

        var executes = reload.getCommand();
        assertNotNull(executes);

        int result = executes.run(ctx);
        assertEquals(1, result);
        Mockito.verify(mockPlugin).refreshConfig();
    }
}
