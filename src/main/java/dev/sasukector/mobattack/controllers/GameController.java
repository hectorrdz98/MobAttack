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
    private @Getter Location arenaArea;
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
        this.currentRound = 0;
        this.random = new Random();
        this.spectatorAreas = new ArrayList<>();
        World overworld = ServerUtilities.getWorld("overworld");
        if (overworld != null) {
            this.spectatorAreas.add(new Location(overworld, 8, 30, -50));
            this.spectatorAreas.add(new Location(overworld, 8, 30, 66));
            this.lootingArea = new Location(overworld, 108, 5, 8);
            this.arenaArea = new Location(overworld, 45, 5, 8);
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

    public void buildWall(Material material) {
        World overworld = ServerUtilities.getWorld("overworld");
        if (overworld != null) {
            for (int y = 4; y <= 23; ++y) {
                for (int z = -38; z <= 54; ++z) {
                    overworld.getBlockAt(8, y, z).setType(material);
                }
            }
        }
    }

    public void teleportAllPlayingPlayers(Location location) {
        World world = location.getWorld();
        TeamsController.getInstance().getPlayingPlayers().forEach(player -> {
            player.teleport(location);
            Bukkit.getScheduler().runTaskLater(MobAttack.getInstance(), () -> {
                world.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                world.spawnParticle(Particle.PORTAL, location, 40);
            }, 5L);
        });
    }

    public void returnLobby() {
        this.currentRound = 0;
        this.currentStatus = Status.LOBBY;
        ServerUtilities.sendBroadcastTitle(
                Component.text("Ha terminado", TextColor.color(0x0096C7)),
                Component.text("la partida", TextColor.color(0x48CAE4))
        );
        Team playingTeam = TeamsController.getInstance().getPlayingTeam();
        for (Player player : TeamsController.getInstance().getEliminatedPlayers()) {
            playingTeam.addEntry(player.getName());
        }
        this.buildWall(Material.AIR);
        TeamsController.getInstance().getPlayingPlayers().forEach(player -> {
            this.restartPlayer(player);
            player.teleport(this.spectatorAreas.get(random.nextInt(this.spectatorAreas.size())));
        });
        Bukkit.getScheduler().runTaskLater(MobAttack.getInstance(), () ->
                ServerUtilities.playBroadcastSound("minecraft:item.trident.return", 1, 0.2f), 5L);
    }

    public void prepareEvent() {
        this.currentRound = 0;
        this.currentStatus = Status.PREPARING;
        ServerUtilities.sendBroadcastTitle(
                Component.text("Momento de", TextColor.color(0x0096C7)),
                Component.text("equiparse", TextColor.color(0x48CAE4))
        );
        this.buildWall(Material.AIR);
        this.teleportAllPlayingPlayers(this.lootingArea);
        ServerUtilities.sendBroadcastAnnounce(
                null,
                ServerUtilities.getMiniMessage().parse(
                        "Prepara tu equipamiento para <bold><color:#219EBC>sobrevivir</color></bold> a las oleadas de enemigos. " +
                                "Tienes <bold><color:#FB8500>10 minutos</color></bold> para prepararte..."
                )
        );
        BossBarController.getInstance().createTimerBossBar(60, "gameWaitingRound");
    }

    public void gameWaitingRound() {
        BossBarController.getInstance().stopCurrentBossBar();
        this.currentRound++;
        this.currentStatus = Status.WAITING;
        this.buildWall(Material.BROWN_STAINED_GLASS);
        this.teleportAllPlayingPlayers(this.arenaArea);
        ServerUtilities.sendBroadcastTitle(
                Component.text("Iniciando", TextColor.color(0x0096C7)),
                Component.text("ronda...", TextColor.color(0x48CAE4))
        );
        ServerUtilities.sendBroadcastAnnounce(
                ServerUtilities.getMiniMessage().parse(
                        "<bold><gradient:#5C4D7D:#B7094C>Oleada #" + this.currentRound + "</gradient></bold>"
                ),
                ServerUtilities.getMiniMessage().parse(
                        "Alista tu inventario, en <bold><color:#FB8500>30 segundos</color></bold> llegarán los mobs..."
                )
        );
        BossBarController.getInstance().createTimerBossBar(30, "gameStartingRound");
    }

    public void gameStartingRound() {
        BossBarController.getInstance().stopCurrentBossBar();
        this.currentStatus = Status.PLAYING;
        this.buildWall(Material.AIR);
        ServerUtilities.sendBroadcastTitle(
                Component.text("Sobrevive...", TextColor.color(0x0096C7)),
                Component.empty()
        );
        ServerUtilities.playBroadcastSound("minecraft:music.effects.board", 1, 1);
    }

}
