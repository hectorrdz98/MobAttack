package dev.sasukector.hundreddaysants.controllers;

import dev.sasukector.hundreddaysants.HundredDaysAnts;
import dev.sasukector.hundreddaysants.helpers.ServerUtilities;
import dev.sasukector.hundreddaysants.models.Hormiguero;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HormiguerosController {

    private static HormiguerosController instance = null;
    private final @Getter List<Hormiguero> hormigueros;
    private @Getter @Setter boolean restartBoss = false;

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
                        "<color:#FFFFFF>Hormiguero [</color><bold><color:#B7094C>" +
                                hormiguero.name() + "</color></bold><color:#FFFFFF>]</color>"
                ));
                Bukkit.getScheduler().runTaskLater(HundredDaysAnts.getInstance(), () ->
                                player.playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1),
                        10L);
            } else {
                ServerUtilities.sendServerMessage(player, "§cNo se pudo ir al hormiguero");
            }
        }
    }

    public void spawnEndBoss() {
        World overworld = ServerUtilities.getWorld("overworld");
        if (overworld == null) return;
        Location location = new Location(overworld, 0, 65, 700);

        WitherSkeleton witherSkeleton = (WitherSkeleton)
                location.getWorld().spawnEntity(location, EntityType.WITHER_SKELETON, CreatureSpawnEvent.SpawnReason.CUSTOM);
        witherSkeleton.customName(Component.text("Hijo de la Dragona", TextColor.color(0xF72585)));
        witherSkeleton.addScoreboardTag("dragon_child");
        witherSkeleton.setCustomNameVisible(true);
        witherSkeleton.setPersistent(true);
        Objects.requireNonNull(witherSkeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(100);
        Objects.requireNonNull(witherSkeleton.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(20);
        witherSkeleton.setHealth(100);

        ItemStack witherHelmet = new ItemStack(Material.PURPLE_STAINED_GLASS);
        witherHelmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        ItemMeta witherHelmetMeta = witherHelmet.getItemMeta();
        witherHelmetMeta.setUnbreakable(true);
        witherHelmet.setItemMeta(witherHelmetMeta);
        witherSkeleton.getEquipment().setHelmet(witherHelmet);
        witherSkeleton.getEquipment().setDropChance(EquipmentSlot.HEAD, 0);

        ItemStack witherBoots = new ItemStack(Material.NETHERITE_BOOTS);
        witherBoots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        ItemMeta witherBootsMeta = witherBoots.getItemMeta();
        witherBootsMeta.setUnbreakable(true);
        witherBoots.setItemMeta(witherBootsMeta);
        witherSkeleton.getEquipment().setBoots(witherBoots);
        witherSkeleton.getEquipment().setDropChance(EquipmentSlot.FEET, 0);

        witherSkeleton.getEquipment().setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
        witherSkeleton.getEquipment().setDropChance(EquipmentSlot.HAND, 0);

        ServerUtilities.sendBroadcastMessage(ServerUtilities.getMiniMessage().parse(
                "<color:#FFFFFF>Ha aparecido el </color><bold><color:#F72585>Hijo de la Dragona</color></bold>"
        ));
        ServerUtilities.playBroadcastSound("minecraft:entity.wither.spawn", 1f, 2f);
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
