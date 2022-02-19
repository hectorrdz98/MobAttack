package dev.sasukector.mobattack.controllers;

import dev.sasukector.mobattack.helpers.ServerUtilities;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private static GameController instance = null;
    private @Getter Status currentStatus = Status.LOBBY;
    private final @Getter List<Location> spectatorAreas;
    private @Getter Location lootingArea;

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public enum Status {
        LOBBY, LOOTING, WAITING, PLAYING
    }

    public GameController() {
        this.spectatorAreas = new ArrayList<>();
        World overworld = ServerUtilities.getWorld("overworld");
        if (overworld != null) {
            this.spectatorAreas.add(new Location(overworld, 8, 30, -50));
            this.spectatorAreas.add(new Location(overworld, 8, 30, 66));
            this.lootingArea = new Location(overworld, 108, 3, 8);
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

}
