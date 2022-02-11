package dev.sasukector.hundreddaysants.events;

import dev.sasukector.hundreddaysants.HundredDaysAnts;
import dev.sasukector.hundreddaysants.controllers.BoardController;
import dev.sasukector.hundreddaysants.controllers.HormiguerosController;
import dev.sasukector.hundreddaysants.controllers.TeamsController;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SpawnEvents implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.joinMessage(
                Component.text("+ ", TextColor.color(0x84E3A4))
                        .append(Component.text(player.getName(), TextColor.color(0x84E3A4)))
        );
        BoardController.getInstance().newPlayerBoard(player);
        if (player.isOp()) {
            TeamsController.getInstance().getMasterTeam().addEntry(player.getName());
        }
        HormiguerosController.getInstance().teleportPlayerToHormiguero(player);
        player.showTitle(Title.title(
                Component.text("Bienvenido", TextColor.color(0xB7094C)),
                Component.text("Al hormiguero", TextColor.color(0x5C4D7D))
        ));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BoardController.getInstance().removePlayerBoard(player);
        event.quitMessage(
                Component.text("- ", TextColor.color(0xE38486))
                        .append(Component.text(player.getName(), TextColor.color(0xE38486)))
        );
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Bukkit.getScheduler().runTaskLater(HundredDaysAnts.getInstance(), () ->
                HormiguerosController.getInstance().teleportPlayerToHormiguero(event.getPlayer()),
                5L);
    }

}
