package dev.sasukector.hundreddaysants.controllers;

import dev.sasukector.hundreddaysants.HundredDaysAnts;
import dev.sasukector.hundreddaysants.helpers.ServerUtilities;
import dev.sasukector.hundreddaysants.models.Hormiguero;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HormiguerosController {

    private static HormiguerosController instance = null;
    private final @Getter List<Hormiguero> hormigueros;

    public static HormiguerosController getInstance() {
        if (instance == null) {
            instance = new HormiguerosController();
        }
        return instance;
    }

    public HormiguerosController() {
        this.hormigueros = new ArrayList<>();
        this.fillHormigueros();
    }

    private void fillHormigueros() {
        World overworld = ServerUtilities.getWorld("overworld");
        if (overworld != null) {
            this.hormigueros.add(new Hormiguero("forest", new Location(overworld, 10, 65, 0), 0));
            this.hormigueros.add(new Hormiguero("jungle", new Location(overworld, 10, 85, 101), 10));
            this.hormigueros.add(new Hormiguero("badlands", new Location(overworld, -2, 93, 200), 20));
            this.hormigueros.add(new Hormiguero("nether_fort", new Location(overworld, -15, 86, 300), 30));
            this.hormigueros.add(new Hormiguero("village", new Location(overworld, 2, 68, 401), 40));
            this.hormigueros.add(new Hormiguero("bastion", new Location(overworld, -51, 85, 499), 50));
            this.hormigueros.add(new Hormiguero("desert", new Location(overworld, 6, 73, 599), 60));
            this.hormigueros.add(new Hormiguero("end", new Location(overworld, 17, 62, 701), 70));
            this.hormigueros.add(new Hormiguero("frozen", new Location(overworld, 34, 64, 798), 80));
            this.hormigueros.add(new Hormiguero("end_city", new Location(overworld, -38, 63, 898), 90));
        }
    }

    public void teleportPlayerToHormiguero(Player player) {
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
    }

    public Hormiguero getHormiguero(String name) {
        Hormiguero foundHormiguero = null;
        for (Hormiguero hormiguero : this.hormigueros) {
            if (hormiguero.name().equals(name)) {
                foundHormiguero = hormiguero;
                break;
            }
        }
        return foundHormiguero;
    }

    public List<Hormiguero> getValidHormigueros() {
        List<Hormiguero> hormigueros = new ArrayList<>();
        World overworld = ServerUtilities.getWorld("overworld");
        if (overworld != null) {
            int days = (int) (overworld.getFullTime() / 24000);
            for (Hormiguero hormiguero : this.hormigueros) {
                if (hormiguero.requiredDays() <= days) {
                    hormigueros.add(hormiguero);
                }
            }
        }
        return hormigueros;
    }

    public Hormiguero getCurrentHormiguero() {
        World overworld = ServerUtilities.getWorld("overworld");
        if (overworld != null) {
            int days = (int) (overworld.getFullTime() / 24000);
            Hormiguero currentHormiguero = null;
            for (Hormiguero hormiguero : this.hormigueros) {
                if (currentHormiguero == null) {
                    currentHormiguero = hormiguero;
                } else if (hormiguero.requiredDays() > currentHormiguero.requiredDays() &&
                        hormiguero.requiredDays() <= days) {
                    currentHormiguero = hormiguero;
                }
            }
            return currentHormiguero;
        }
        return null;
    }

}
