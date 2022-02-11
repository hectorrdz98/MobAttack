package dev.sasukector.hundreddaysants.events;

import dev.sasukector.hundreddaysants.HundredDaysAnts;
import dev.sasukector.hundreddaysants.controllers.BoardController;
import dev.sasukector.hundreddaysants.controllers.HormiguerosController;
import dev.sasukector.hundreddaysants.controllers.TeamsController;
import dev.sasukector.hundreddaysants.helpers.ServerUtilities;
import dev.sasukector.hundreddaysants.models.Hormiguero;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        Hormiguero hormiguero =  HormiguerosController.getInstance().getCurrentHormiguero();
        if (hormiguero != null) {
            Location location = hormiguero.location();
            if (location != null) {
                player.teleport(location);
                player.sendActionBar(ServerUtilities.getMiniMessage().parse(
                        "<color:#5C4D7D>Hormiguero [</color><color:#B7094C>" +
                                hormiguero.name() + "</color><color>]</color>"
                ));
                Bukkit.getScheduler().runTaskLater(HundredDaysAnts.getInstance(), () ->
                        player.playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1),
                        10L);
            } else {
                ServerUtilities.sendServerMessage(player, "§cNo se pudo ir al hormiguero");
            }
        }
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

}
