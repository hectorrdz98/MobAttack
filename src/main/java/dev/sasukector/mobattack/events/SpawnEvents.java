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
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.InventoryHolder;

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
                GameController.getInstance().restartPlayer(player);
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

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            if (GameController.getInstance().getCurrentStatus() == GameController.Status.LOBBY) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (GameController.getInstance().getCurrentStatus() == GameController.Status.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (GameController.getInstance().getCurrentStatus() == GameController.Status.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if (GameController.getInstance().getCurrentStatus() == GameController.Status.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler
    public void blockChestInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof InventoryHolder) {
                if (GameController.getInstance().getCurrentStatus() == GameController.Status.LOBBY) {
                    if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
                        event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDroppedItem(PlayerDropItemEvent event) {
        if (GameController.getInstance().getCurrentStatus() == GameController.Status.LOBBY) {
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemEaten(PlayerItemConsumeEvent event) {
        if (GameController.getInstance().getCurrentStatus() == GameController.Status.LOBBY) {
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickedUpItems(PlayerAttemptPickupItemEvent event) {
        if (GameController.getInstance().getCurrentStatus() == GameController.Status.LOBBY) {
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
                event.setCancelled(true);
        }
    }

}