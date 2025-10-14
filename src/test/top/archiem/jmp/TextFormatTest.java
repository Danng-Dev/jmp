package top.archiem.jmp;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class textFormatTest {

    @Test
    void testFormatWithoutPAPI() {
        Player player = Mockito.mock(Player.class);
        Mockito.when(player.getName()).thenReturn("Steve");

        textFormat tf = new textFormat(false);
        Component result = tf.format("Welcome, %player%!", player);

        assertTrue(result.toString().contains("Steve"));
    }

    @Test
    void testFormatWithPAPI_ReplacesPlaceholders() {
        Player player = Mockito.mock(Player.class);
        Mockito.when(player.getName()).thenReturn("Alex");

        // Mock PlaceholderAPI static method
        try (MockedStatic<PlaceholderAPI> mocked = Mockito.mockStatic(PlaceholderAPI.class)) {
            mocked.when(() -> PlaceholderAPI.setPlaceholders(player, "Hello %player%!"))
                  .thenReturn("Hello Alex!");

            textFormat tf = new textFormat(true);
            Component result = tf.format("Hello %player%!", player);

            assertTrue(result.toString().contains("Alex"));
        }
    }

    @Test
    void testFormatWithPAPIFallbackIfException() {
        Player player = Mockito.mock(Player.class);
        Mockito.when(player.getName()).thenReturn("Steve");

        try (MockedStatic<PlaceholderAPI> mocked = Mockito.mockStatic(PlaceholderAPI.class)) {
            mocked.when(() -> PlaceholderAPI.setPlaceholders(player, "Welcome %player%!"))
                  .thenThrow(new RuntimeException("PAPI failed"));

            textFormat tf = new textFormat(true);
            Component result = tf.format("Welcome %player%!", player);

            assertNotNull(result);
        }
    }
}
