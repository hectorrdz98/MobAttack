package dev.sasukector.mobattack.events;

import dev.sasukector.mobattack.MobAttack;
import dev.sasukector.mobattack.controllers.BoardController;
import dev.sasukector.mobattack.controllers.GameController;
import dev.sasukector.mobattack.controllers.TeamsController;
import dev.sasukector.mobattack.helpers.ServerUtilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

public class SpawnEvents implements Listener {

    private final Random random = new Random();

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
        Bukkit.getScheduler().runTaskLater(MobAttack.getInstance(), () -> {
            String message = "Es momento de armarse";
            Location spawnLocation = GameController.getInstance().getLootingArea();
            if (GameController.getInstance().getCurrentStatus() != GameController.Status.LOOTING) {
                message = "Estás observando la partida";
                spawnLocation = GameController.getInstance().getSpectatorAreas()
                        .get(random.nextInt(GameController.getInstance().getSpectatorAreas().size()));
            }
            player.playSound(player.getLocation(), "minecraft:block.note_block.bell", 1, 1);
            player.showTitle(Title.title(
                    Component.text("La invasión...", TextColor.color(0xB7094C)),
                    Component.text("Cuidado...", TextColor.color(0x5C4D7D))
            ));
            player.teleport(spawnLocation);
            ServerUtilities.sendServerMessage(player, message);

        }, 5L);
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
