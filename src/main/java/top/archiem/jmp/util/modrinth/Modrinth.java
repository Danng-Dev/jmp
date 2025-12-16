package top.archiem.jmp.util.modrinth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import top.archiem.jmp.JMP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class Modrinth implements Listener {

    private static final String VERSIONS_URL = "https://api.modrinth.com/v2/project/jmp/version";
    private static ModrinthVersion newVersion = null;
    private static final Set<UUID> notifiedPlayers = new HashSet<>();
    private static JMP plugin; // Reference to the main plugin instance

    // Private constructor to prevent direct instantiation, enforcing singleton-like behavior
    // for event registration.
    private Modrinth() {}

    public static void initialize(JMP pluginInstance) {
        plugin = pluginInstance;
        // Register this class as a listener for Bukkit events
        Bukkit.getPluginManager().registerEvents(new Modrinth(), plugin);
        loadVersion();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!canSendNotification(player)) return;

        // Delay notification asynchronously to avoid blocking the join event
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            notifyPlayer(player);
        }, 5 * 20L); // 5 seconds * 20 ticks/second
    }

    private static void loadVersion() {
        HttpRequest request = HttpRequest.newBuilder(URI.create(VERSIONS_URL))
                .GET()
                .build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() != 200) {
                        plugin.getLogger().warning("Failed to fetch Modrinth versions. Status code: " + response.statusCode());
                        return;
                    }

                    Gson gson = new GsonBuilder()
                            .setDateFormat(DateFormat.FULL, DateFormat.FULL)
                            .create();

                    List<ModrinthVersion> versions = gson.fromJson(
                            response.body(),
                            new TypeToken<List<ModrinthVersion>>() {}.getType()
                    );

                    ModrinthVersion latestVersion = findLatestVersion(versions);
                    if (latestVersion == null) {
                        plugin.getLogger().info("No new Modrinth version found or could not determine latest.");
                        return;
                    }

                    // Assuming plugin.getDescription().getVersion() returns the current plugin version string
                    SemanticVersion currentPluginVersion = SemanticVersion.fromString(plugin.getDescription().getVersion());

                    if (latestVersion.getVersionNumber().compareTo(currentPluginVersion) <= 0) {
                        plugin.getLogger().info("Current version is up to date or newer.");
                        return;
                    }

                    newVersion = latestVersion;

                    plugin.getLogger().log(Level.SEVERE,
                            StringUtil.trimMargin(
                                    """
                                    |-----------------{  Join Message Plus Update }-----------------
                                    |    A new version of  Join Message Plus is available!
                                    |    Current version: %s
                                    |    New version:     %s
                                    |    Download it at:  %s
                                    |------------------------------------------------------
                                    """.formatted(
                                            plugin.getDescription().getVersion(),
                                            latestVersion.getVersionNumber().toString(),
                                            latestVersion.getUrl()
                                    )
                            )
                    );

                    // Notify all online players on the main thread
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        Bukkit.getOnlinePlayers().forEach(Modrinth::notifyPlayer);
                    });

                })
                .exceptionally(e -> {
                    plugin.getLogger().log(Level.SEVERE, "Error fetching Modrinth versions: " + e.getMessage(), e);
                    return null;
                });
    }

    private static ModrinthVersion findLatestVersion(List<ModrinthVersion> versions) {
        boolean currentIsDevelopment = plugin.getDescription().getVersion().contains("dev");

        return versions.stream()
                .filter(v -> currentIsDevelopment || v.getVersionType().equals("release"))
                .max((v1, v2) -> v1.getDatePublished().compareTo(v2.getDatePublished()))
                .orElse(null);
    }

    private static boolean canSendNotification(Player player) {
        if (newVersion == null) return false;
        if (notifiedPlayers.contains(player.getUniqueId())) return false;
        return player.hasPermission("jmp.update");
    }

    private static void notifyPlayer(Player player) {
        if (!canSendNotification(player)) return;
        ModrinthVersion currentNewVersion = newVersion; // Capture for lambda
        if (currentNewVersion == null) return; // Should not happen due to canSendNotification check

        notifiedPlayers.add(player.getUniqueId());

        // Using MiniMessage for rich text formatting (requires Adventure API)
        MiniMessage miniMessage = MiniMessage.miniMessage();
        Component message = miniMessage.deserialize(
                StringUtil.trimMargin(
                        """
                        |<st><gray>             </st><gray>{ <dark_gray><bold> Join Message Plus Update</bold><gray> }<st>             </st>
                        |
                        |    A new version of  Join Message Plus is available!
                        |    <red>Current version: <reset>%s<reset>
                        |    <green>New version:       <reset>%s<reset>
                        |    <blue>Download it:       <reset><bold><click:open_url:%s><hover:show_text:Click to open>[Here]<reset>
                        |    
                        |<st><gray>                                                      </st>
                        """.formatted(
                                plugin.getDescription().getVersion(),
                                currentNewVersion.getVersionNumber().toString(),
                                currentNewVersion.getUrl()
                        )
                )
        );
        player.sendMessage(message);
    }
}
