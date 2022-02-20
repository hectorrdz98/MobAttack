package dev.sasukector.mobattack.controllers;

import dev.sasukector.mobattack.helpers.ServerUtilities;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaveController {

    private static WaveController instance = null;
    private final @Getter int maxWaves;
    private @Getter int maxEntities;
    private final @Getter List<Entity> waveEntities;
    private final Random random = new Random();
    private @Getter WaveType currentWaveType;

    public static WaveController getInstance() {
        if (instance == null) {
            instance = new WaveController();
        }
        return instance;
    }

    public enum WaveType {
        NORMAL, BOSS
    }

    public WaveController() {
        this.maxWaves = 2;
        this.maxEntities = 0;
        this.waveEntities = new ArrayList<>();
    }

    public Location getRandomLocation(World world) {
        // Spawn square [-56, 5, 45] [7, 5, -29]
        int x = random.nextInt(64) - 56;
        int z = random.nextInt(75) - 29;
        return new Location(world, x, 5, z);
    }

    public void deleteItemsOnGround() {
        World overworld = ServerUtilities.getWorld("overworld");
        if (overworld != null) {
            for (Entity entity : overworld.getEntities()) {
                if (entity instanceof Item) {
                    entity.remove();
                }
            }
        }
    }

    public void deleteWave() {
        for (Entity entity : this.waveEntities) {
            entity.remove();
        }
        this.waveEntities.clear();
    }

    public void createWave() {
        int waveNumber = GameController.getInstance().getCurrentRound();
        World overworld = ServerUtilities.getWorld("overworld");
        if (overworld != null) {
            switch (waveNumber) {
                case 1 -> this.generateWave1(overworld);
                case 2 -> this.generateWave2(overworld);
            }
        }
    }

    public void basicEntityConfiguration(LivingEntity livingEntity) {
        livingEntity.setCanPickupItems(false);
        livingEntity.setPersistent(true);
        livingEntity.setRemoveWhenFarAway(false);
        EntityEquipment entityEquipment = livingEntity.getEquipment();
        if (entityEquipment != null) {
            entityEquipment.setDropChance(EquipmentSlot.HEAD, 0);
            entityEquipment.setDropChance(EquipmentSlot.CHEST, 0);
            entityEquipment.setDropChance(EquipmentSlot.LEGS, 0);
            entityEquipment.setDropChance(EquipmentSlot.FEET, 0);
            entityEquipment.setDropChance(EquipmentSlot.HAND, 0);
            entityEquipment.setDropChance(EquipmentSlot.OFF_HAND, 0);
        }
    }

    public void generateWave1(World world) {
        this.currentWaveType = WaveType.NORMAL;
        this.maxEntities = 0;
        for (int i = 0; i < 50; ++i) {
            Zombie zombie = (Zombie) world.spawnEntity(this.getRandomLocation(world), EntityType.ZOMBIE);
            this.waveEntities.add(zombie);
            this.maxEntities++;
            zombie.customName(Component.text("Zomwhi", TextColor.color(0x98F5E1)));
            this.basicEntityConfiguration(zombie);
            zombie.setAdult();
            zombie.getEquipment().setHelmet(null);
            zombie.getEquipment().setChestplate(null);
            zombie.getEquipment().setLeggings(null);
            zombie.getEquipment().setBoots(null);
            ItemStack hand = null;
            if (random.nextFloat() >= 0.9) {
                hand = new ItemStack(Material.STONE_SWORD);
            } else if (random.nextFloat() >= 0.7) {
                hand = new ItemStack(Material.WOODEN_SWORD);
            }
            zombie.getEquipment().setItemInMainHand(hand);
        }
    }

    public void generateWave2(World world) {
        this.currentWaveType = WaveType.NORMAL;
        this.maxEntities = 0;
        for (int i = 0; i < 50; ++i) {
            Skeleton skeleton = (Skeleton) world.spawnEntity(this.getRandomLocation(world), EntityType.SKELETON);
            this.waveEntities.add(skeleton);
            this.maxEntities++;
            skeleton.customName(Component.text("Veganito", TextColor.color(0xB9FBC0)));
            this.basicEntityConfiguration(skeleton);
            skeleton.getEquipment().setHelmet(null);
            skeleton.getEquipment().setChestplate(null);
            skeleton.getEquipment().setLeggings(null);
            skeleton.getEquipment().setBoots(null);
            ItemStack hand = new ItemStack(Material.BOW);
            if (random.nextFloat() >= 0.9) {
                hand.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
            }
            if (random.nextFloat() >= 0.7) {
                hand.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
            }
            skeleton.getEquipment().setItemInMainHand(hand);
        }
    }

}
