package dev.sasukector.mobattack.controllers;

import dev.sasukector.mobattack.MobAttack;
import dev.sasukector.mobattack.helpers.ServerUtilities;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {

    private static GameController instance = null;
    private @Getter Status currentStatus = Status.LOBBY;
    private final @Getter List<Location> spectatorAreas;
    private @Getter Location lootingArea;
    private final Random random;
    private @Getter @Setter int currentRound;

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public enum Status {
        LOBBY, PREPARING, WAITING, PLAYING
    }

    public GameController() {
        this.currentRound = 1;
        this.random = new Random();
        this.spectatorAreas = new ArrayList<>();
        World overworld = ServerUtilities.getWorld("overworld");
        if (overworld != null) {
            this.spectatorAreas.add(new Location(overworld, 8, 30, -50));
            this.spectatorAreas.add(new Location(overworld, 8, 30, 66));
            this.lootingArea = new Location(overworld, 108, 5, 8);
        }
    }

    public void restartPlayer(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);
        player.setArrowsInBody(0);
        player.setFireTicks(0);
        player.setVisualFire(false);
        player.setAllowFlight(false);
        player.setCollidable(true);
        player.getActivePotionEffects().forEach(p -> player.removePotionEffect(p.getType()));
        player.getInventory().clear();
        player.updateInventory();
    }

    public void returnLobby() {
        this.currentRound = 1;
        this.currentStatus = Status.LOBBY;
        ServerUtilities.playBroadcastSound("minecraft:item.trident.return", 1, 0.2f);
        ServerUtilities.sendBroadcastTitle(
                Component.text("Ha terminado", TextColor.color(0x0096C7)),
                Component.text("la partida", TextColor.color(0x48CAE4))
        );
        Team playingTeam = TeamsController.getInstance().getPlayingTeam();
        for (Player player : TeamsController.getInstance().getEliminatedPlayers()) {
            playingTeam.addEntry(player.getName());
        }
        TeamsController.getInstance().getPlayingPlayers().forEach(player -> {
            this.restartPlayer(player);
            player.teleport(this.spectatorAreas.get(random.nextInt(this.spectatorAreas.size())));
        });
    }

    public void prepareEvent() {
        this.currentRound = 1;
        this.currentStatus = Status.PREPARING;
        ServerUtilities.playBroadcastSound("minecraft:item.armor.equip_elytra", 1, 0.2f);
        ServerUtilities.sendBroadcastTitle(
                Component.text("Momento de", TextColor.color(0x0096C7)),
                Component.text("equiparse", TextColor.color(0x48CAE4))
        );
        World world = this.lootingArea.getWorld();
        TeamsController.getInstance().getPlayingPlayers().forEach(player -> {
            player.teleport(this.lootingArea);
            Bukkit.getScheduler().runTaskLater(MobAttack.getInstance(), () -> {
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                world.playSound(this.lootingArea, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            }, 5L);
        });
        ServerUtilities.sendBroadcastAnnounce(
                ServerUtilities.getMiniMessage().parse(
                        "Prepara tu equipamiento para <bold><color:#219EBC>sobrevivir</color></bold> a las oleadas de enemigos. " +
                                "Tienes <bold><color:#FB8500>10 minutos</color></bold> para prepararte"
                )
        );
        BossBarController.getInstance().createTimerBossBar(60 * 2);
    }

    public void gameStartedEvent() {
        BossBarController.getInstance().stopCurrentBossBar();
    }

}
