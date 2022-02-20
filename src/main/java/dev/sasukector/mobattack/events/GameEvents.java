package dev.sasukector.mobattack.events;

import dev.sasukector.mobattack.MobAttack;
import dev.sasukector.mobattack.controllers.GameController;
import dev.sasukector.mobattack.controllers.TeamsController;
import dev.sasukector.mobattack.helpers.ServerUtilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.Random;

public class GameEvents implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onPlayerDismount(EntityDismountEvent event) {
        if (event.getEntity() instanceof Player player && event.getDismounted() instanceof ArmorStand armorStand) {
            if (armorStand.getScoreboardTags().contains("chair")) {
                armorStand.remove();
                player.playSound(player.getLocation(), Sound.ENTITY_PIG_SADDLE, 1, 1.6f);
                player.sendActionBar(ServerUtilities.getMiniMessage().parse(
                        "<color:#FED9B7>Te has levantado</color>"
                ));
                player.teleport(player.getLocation().add(0, 0.5, 0));
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        if (TeamsController.getInstance().isPlaying(player)) {
            ServerUtilities.sendBroadcastMessage(ServerUtilities.getMiniMessage().parse(
                    "<bold><color:#F94144>" + player.getName() +
                            "</color></bold> <color:#F94144>no ha logrado sobrevivir...</color>"
            ));
            player.showTitle(Title.title(
                    Component.text("Has sido", TextColor.color(0xB7094C)),
                    Component.text("eliminado...", TextColor.color(0x5C4D7D))
            ));
            TeamsController.getInstance().getEliminatedTeam().addEntry(player.getName());
            if (TeamsController.getInstance().getPlayingPlayers().size() <= 1) {
                GameController.getInstance().gameWinner();
            }
        }
        GameController.getInstance().restartPlayer(player);
        Location location = GameController.getInstance().getSpectatorAreas()
                .get(random.nextInt(GameController.getInstance().getSpectatorAreas().size()));
        player.teleport(location);
        World world = ServerUtilities.getWorld("overworld");
        if (world != null) {
            Bukkit.getScheduler().runTaskLater(MobAttack.getInstance(), () -> {
                world.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                world.spawnParticle(Particle.PORTAL, location, 40);
            }, 5L);
        }
    }

}
