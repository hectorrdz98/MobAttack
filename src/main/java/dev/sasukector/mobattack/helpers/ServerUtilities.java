package dev.sasukector.mobattack.helpers;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ServerUtilities {

    private final static @Getter MiniMessage miniMessage = MiniMessage.get();
    private static @Getter @Setter Location lobbySpawn = null;

    // Associate all world names
    private final static Map<String, String> worldsNames;
    static {
        worldsNames = new HashMap<>();
        worldsNames.put("overworld", "world");
        worldsNames.put("nether", "world_nether");
        worldsNames.put("end", "world_the_end");
    }

    public static World getWorld(String worldName) {
        if (worldsNames.containsKey(worldName)) {
            return Bukkit.getWorld(worldsNames.get(worldName));
        }
        return null;
    }

    public static Component getPluginNameColored() {
        return miniMessage.parse("<bold><gradient:#5C4D7D:#B7094C>La invasión</gradient></bold>");
    }

    public static void sendBroadcastTitle(Component title, Component subtitle) {
        Title titleToShow = Title.title(title, subtitle);
        Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(titleToShow));
    }

    public static void sendServerMessage(Player player, String message) {
        player.sendMessage(getPluginNameColored()
                .append(Component.text(" ▶ ", TextColor.color(0xC0C1C2)))
                .append(Component.text(message, TextColor.color(0xFFFFFF))));
    }

    public static void sendServerMessage(Player player, Component message) {
        player.sendMessage(getPluginNameColored()
                .append(Component.text(" ▶ ", TextColor.color(0xC0C1C2)))
                .append(message));
    }

    public static void sendBroadcastMessage(Component message) {
        Bukkit.broadcast(getPluginNameColored()
                .append(Component.text(" ▶ ", TextColor.color(0xC0C1C2)))
                .append(message));
    }

    public static void sendBroadcastAnnounce(Component title, Component message) {
        playBroadcastSound("minecraft:music.effects.line", 1, 1);
        for (int i = 0; i < 2; ++i) {
            Bukkit.broadcast(Component.empty());
        }
        if (title != null) {
            Bukkit.broadcast(title);
        } else {
            Bukkit.broadcast(getPluginNameColored());
        }
        Bukkit.broadcast(message);
        for (int i = 0; i < 2; ++i) {
            Bukkit.broadcast(Component.empty());
        }
    }

    public static void playBroadcastSound(String sound, float volume, float pitch) {
        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), sound, volume, pitch));
    }

}
