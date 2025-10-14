package top.archiem.jmp;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class JMPTest {

    private ServerMock server;
    private JMP plugin;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(JMP.class);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testPluginLoads() {
        assertNotNull(plugin);
        assertTrue(plugin.isEnabled());
    }

    @Test
    void testOnEnableSetsPapienabledFalseWhenNoPlaceholderAPI() {
        assertFalse(plugin.papienabled, "PlaceholderAPI should not be detected");
    }

    @Test
    void testRefreshConfigReturnsTrue() {
        assertTrue(plugin.refreshConfig());
    }

    @Test
    void testJoinEvent_DefaultJoinMessage() {
        PlayerMock player = server.addPlayer("Steve");
        PlayerJoinEvent event = new PlayerJoinEvent(player, null);

        plugin.joinMsg = "Welcome %player%!";
        plugin.msgPlayer = true;
        plugin.msgPlayerJoin = "Hello %player%!";

        server.getPluginManager().callEvent(event);

        // Player should receive formatted join message
        player.assertSaid("Hello Steve!");
        assertNotNull(event.joinMessage());
    }

    @Test
    void testLeaveEvent_DefaultQuitMessage() {
        PlayerMock player = server.addPlayer("Alex");
        PlayerQuitEvent event = new PlayerQuitEvent(player, null);

        plugin.leaveMsg = "Goodbye %player%!";
        plugin.msgPlayer = true;
        plugin.msgPlayerLeave = "See you soon, %player%!";

        server.getPluginManager().callEvent(event);

        player.assertSaid("See you soon, Alex!");
        assertNotNull(event.quitMessage());
    }

    @Test
    void testSilentJoinMessage() {
        PlayerMock player = server.addPlayer("Silent");
        player.addAttachment(plugin, "JMP.silent", true);
        plugin.msgPlayer = true;
        plugin.msgPlayerJoin = "Join message test";
        plugin.silentMsg = "Silent join.";

        PlayerJoinEvent event = new PlayerJoinEvent(player, null);
        server.getPluginManager().callEvent(event);

        // Silent players get no join message but do get a private message
        assertNull(event.joinMessage());
        player.assertSaid("Silent join.");
    }

    @Test
    void testPluginRegistersListener() {
        PluginManager pm = server.getPluginManager();
        assertTrue(pm.isPluginEnabled(plugin));
    }
}
