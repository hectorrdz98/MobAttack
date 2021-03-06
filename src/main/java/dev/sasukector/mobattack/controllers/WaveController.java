package dev.sasukector.mobattack.controllers;

import dev.sasukector.mobattack.MobAttack;
import dev.sasukector.mobattack.helpers.ServerUtilities;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class WaveController {

    private static WaveController instance = null;
    private final @Getter int maxWaves;
    private @Getter int maxEntities;
    private final @Getter List<LivingEntity> waveEntities;
    private final Random random = new Random();
    private @Getter WaveType currentWaveType;
    private static final @Getter String tagInmuneToExplosions = "inmuneToExplosions";
    private static final @Getter String tagInmuneToArrows = "inmuneToArrows";

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
        this.maxWaves = 9;
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
        List<LivingEntity> livingEntities = this.waveEntities.stream().toList();
        for (LivingEntity livingEntity : livingEntities) {
            try {
                livingEntity.remove();
            } catch (Exception ignored) {}
        }
        this.waveEntities.clear();
        World overworld = ServerUtilities.getWorld("overworld");
        if (overworld != null) {
            for (Entity entity : overworld.getEntities()) {
                if (entity instanceof LivingEntity && !(entity instanceof Player) &&
                    !(entity instanceof ArmorStand)) {
                    entity.remove();
                }
            }
        }
    }

    public void createWave() {
        int waveNumber = GameController.getInstance().getCurrentRound();
        World overworld = ServerUtilities.getWorld("overworld");
        String waveTitle = "";
        if (overworld != null) {
            switch (waveNumber) {
                case 1 -> {
                    this.generateWave1(overworld);
                    waveTitle = "Los Zomwhis infinitos";
                }
                case 2 -> {
                    this.generateWave2(overworld);
                    waveTitle = "Cientos de veganos";
                }
                case 3 -> {
                    this.generateWave3(overworld);
                    waveTitle = "Los finos";
                }
                case 4 -> {
                    this.generateWave4(overworld);
                    waveTitle = "Casi humanos...";
                }
                case 5 -> {
                    this.generateWave5(overworld);
                    waveTitle = "El rey del nether";
                }
                case 6 -> {
                    this.generateWave6(overworld);
                    waveTitle = "Eternos";
                }
                case 7 -> {
                    this.generateWave7(overworld);
                    waveTitle = "??Pum pum? Cuidado...";
                }
                case 8 -> {
                    this.generateWave8(overworld);
                    waveTitle = "??C-conter?";
                }
                case 9 -> {
                    this.generateWave9(overworld);
                    waveTitle = "La venganza";
                }
            }
        }
        ServerUtilities.sendBroadcastTitle(
                Component.text("Oleada #" + waveNumber, TextColor.color(0x0096C7)),
                Component.text(waveTitle, TextColor.color(0x48CAE4))
        );
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
        for (int i = 0; i < 250; ++i) {
            Zombie zombie = (Zombie) world.spawnEntity(this.getRandomLocation(world), EntityType.ZOMBIE);
            this.waveEntities.add(zombie);
            this.maxEntities++;
            zombie.customName(Component.text("Zomwhi", TextColor.color(0x98F5E1)));
            this.basicEntityConfiguration(zombie);
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(100f);
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.35f);
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(20);
            zombie.setHealth(100f);
            zombie.setAdult();
            zombie.getEquipment().setHelmet(null);
            zombie.getEquipment().setChestplate(null);
            zombie.getEquipment().setLeggings(null);
            zombie.getEquipment().setBoots(null);
            ItemStack hand;
            if (random.nextFloat() >= 0.9) {
                hand = new ItemStack(Material.NETHERITE_SWORD);
            } else if (random.nextFloat() >= 0.7) {
                hand = new ItemStack(Material.DIAMOND_SWORD);
            } else {
                hand = new ItemStack(Material.IRON_SWORD);
            }
            if (random.nextFloat() >= 0.9) {
                hand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 4);
            } else {
                hand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
            }
            if (random.nextFloat() >= 0.9) {
                hand.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
            }
            zombie.getEquipment().setItemInMainHand(hand);
        }
    }

    public void generateWave2(World world) {
        this.currentWaveType = WaveType.NORMAL;
        this.maxEntities = 0;
        for (int i = 0; i < 250; ++i) {
            Skeleton skeleton = (Skeleton) world.spawnEntity(this.getRandomLocation(world), EntityType.SKELETON);
            this.waveEntities.add(skeleton);
            this.maxEntities++;
            skeleton.customName(Component.text("Veganito", TextColor.color(0xB9FBC0)));
            this.basicEntityConfiguration(skeleton);
            Objects.requireNonNull(skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(100f);
            Objects.requireNonNull(skeleton.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.35f);
            Objects.requireNonNull(skeleton.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(20);
            skeleton.setHealth(100f);
            skeleton.getEquipment().setHelmet(null);
            skeleton.getEquipment().setChestplate(null);
            skeleton.getEquipment().setLeggings(null);
            skeleton.getEquipment().setBoots(null);
            ItemStack hand = new ItemStack(Material.BOW);
            if (random.nextFloat() >= 0.9) {
                hand.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 12);
            } else if (random.nextFloat() >= 0.7) {
                hand.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 10);
            } else {
                hand.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 8);
            }
            if (random.nextFloat() >= 0.7) {
                hand.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
            }
            if (random.nextFloat() >= 0.7) {
                hand.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 2);
            }
            skeleton.getEquipment().setItemInMainHand(hand);
        }
    }

    public void generateWave3(World world) {
        this.currentWaveType = WaveType.NORMAL;
        this.maxEntities = 0;
        for (int i = 0; i < 100; ++i) {
            Zombie zombie = (Zombie) world.spawnEntity(this.getRandomLocation(world), EntityType.ZOMBIE);
            this.waveEntities.add(zombie);
            this.maxEntities++;
            zombie.customName(Component.text("Zomwhi", TextColor.color(0x98F5E1)));
            this.basicEntityConfiguration(zombie);
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(120f);
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.35f);
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(20);
            zombie.setHealth(120f);
            zombie.setAdult();
            zombie.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
            zombie.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            zombie.getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
            zombie.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
            ItemStack hand;
            if (random.nextFloat() >= 0.6) {
                hand = new ItemStack(Material.NETHERITE_SWORD);
            } else if (random.nextFloat() >= 0.4) {
                hand = new ItemStack(Material.DIAMOND_SWORD);
            } else {
                hand = new ItemStack(Material.IRON_SWORD);
            }
            if (random.nextFloat() >= 0.6) {
                hand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
            } else if (random.nextFloat() >= 0.4) {
                hand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 8);
            } else {
                hand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
            }
            if (random.nextFloat() >= 0.6) {
                hand.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
            }
            zombie.getEquipment().setItemInMainHand(hand);
        }
        for (int i = 0; i < 100; ++i) {
            Zombie zombie = (Zombie) world.spawnEntity(this.getRandomLocation(world), EntityType.ZOMBIE);
            this.waveEntities.add(zombie);
            this.maxEntities++;
            zombie.customName(Component.text("Fino", TextColor.color(0xCFBAF0)));
            this.basicEntityConfiguration(zombie);
            zombie.setAdult();
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(150f);
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.4f);
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(15);
            zombie.setHealth(150f);
            zombie.getEquipment().setHelmet(null);
            zombie.getEquipment().setChestplate(null);
            zombie.getEquipment().setLeggings(null);
            zombie.getEquipment().setBoots(null);
            ItemStack hand = new ItemStack(Material.NETHERITE_SWORD);
            hand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 8);
            if (random.nextFloat() >= 0.9) {
                hand.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
            }
            zombie.getEquipment().setItemInMainHand(hand);
        }
        for (int i = 0; i < 100; ++i) {
            Skeleton skeleton = (Skeleton) world.spawnEntity(this.getRandomLocation(world), EntityType.SKELETON);
            this.waveEntities.add(skeleton);
            this.maxEntities++;
            skeleton.customName(Component.text("Veganito", TextColor.color(0xB9FBC0)));
            this.basicEntityConfiguration(skeleton);
            Objects.requireNonNull(skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(100f);
            Objects.requireNonNull(skeleton.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.4f);
            Objects.requireNonNull(skeleton.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(15);
            skeleton.setHealth(100f);
            skeleton.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
            skeleton.getEquipment().setChestplate(null);
            skeleton.getEquipment().setLeggings(null);
            skeleton.getEquipment().setBoots(null);
            ItemStack hand = new ItemStack(Material.BOW);
            hand.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 10);
            if (random.nextFloat() >= 0.7) {
                hand.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
            }
            if (random.nextFloat() >= 0.7) {
                hand.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
            }
            skeleton.getEquipment().setItemInMainHand(hand);
        }
    }

    public void generateWave4(World world) {
        this.currentWaveType = WaveType.NORMAL;
        this.maxEntities = 0;
        for (int i = 0; i < 150; ++i) {
            Zombie zombie = (Zombie) world.spawnEntity(this.getRandomLocation(world), EntityType.ZOMBIE);
            this.waveEntities.add(zombie);
            this.maxEntities++;
            zombie.customName(Component.text("Fino", TextColor.color(0xCFBAF0)));
            this.basicEntityConfiguration(zombie);
            zombie.setAdult();
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(160f);
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.4f);
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(15);
            zombie.setHealth(160f);
            zombie.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
            zombie.getEquipment().setChestplate(null);
            zombie.getEquipment().setLeggings(null);
            zombie.getEquipment().setBoots(null);
            ItemStack hand = new ItemStack(Material.DIAMOND_SWORD);
            if (random.nextFloat() >= 0.7) {
                hand.setType(Material.NETHERITE_SWORD);
            }
            hand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 8);
            if (random.nextFloat() >= 0.5) {
                hand.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
            }
            zombie.getEquipment().setItemInMainHand(hand);
        }
        for (int i = 0; i < 100; ++i) {
            Vindicator vindicator = (Vindicator) world.spawnEntity(this.getRandomLocation(world), EntityType.VINDICATOR);
            this.waveEntities.add(vindicator);
            this.maxEntities++;
            vindicator.customName(Component.text("Casi Humano", TextColor.color(0xCB997E)));
            this.basicEntityConfiguration(vindicator);
            Objects.requireNonNull(vindicator.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(150f);
            Objects.requireNonNull(vindicator.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.4f);
            Objects.requireNonNull(vindicator.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(15);
            vindicator.setHealth(150f);
            vindicator.getEquipment().setHelmet(null);
            vindicator.getEquipment().setChestplate(null);
            vindicator.getEquipment().setLeggings(null);
            vindicator.getEquipment().setBoots(null);
            ItemStack hand = new ItemStack(Material.NETHERITE_AXE);
            if (random.nextFloat() >= 0.8) {
                hand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 8);
            } else {
                hand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
            }
            vindicator.getEquipment().setItemInMainHand(hand);
        }
    }

    public void generateWave5(World world) {
        this.currentWaveType = WaveType.BOSS;
        this.maxEntities = 1;
        WitherSkeleton boss = (WitherSkeleton) world.spawnEntity(this.getRandomLocation(world), EntityType.WITHER_SKELETON);
        this.waveEntities.add(boss);
        boss.customName(Component.text("El rey del nether", TextColor.color(0xCFBAF0)));
        boss.setCustomNameVisible(true);
        this.basicEntityConfiguration(boss);
        Objects.requireNonNull(boss.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(2000f);
        Objects.requireNonNull(boss.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.35f);
        Objects.requireNonNull(boss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(15);
        Objects.requireNonNull(boss.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(30);
        Objects.requireNonNull(boss.getAttribute(Attribute.GENERIC_ATTACK_SPEED)).setBaseValue(10);
        boss.setHealth(2000f);
        boss.getEquipment().setHelmet(null);
        boss.getEquipment().setChestplate(null);
        boss.getEquipment().setLeggings(null);
        boss.getEquipment().setBoots(null);

        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.setUnbreakable(true);
        sword.setItemMeta(swordMeta);
        boss.getEquipment().setItemInMainHand(sword);

        ItemStack bow = new ItemStack(Material.BOW);
        bow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 15);
        ItemMeta bowMeta = bow.getItemMeta();
        bowMeta.setUnbreakable(true);
        bow.setItemMeta(bowMeta);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (boss.isDead()) {
                    cancel();
                } else {
                    if (GameController.getInstance().getCurrentStatus() == GameController.Status.PLAYING) {
                        if (random.nextFloat() >= 0.3) {
                            if (random.nextFloat() >= 0.5) {
                                Player player = TeamsController.getInstance().getPlayingPlayers()
                                        .get(random.nextInt(TeamsController.getInstance().getPlayingPlayers().size()));
                                world.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                                world.spawnParticle(Particle.PORTAL, player.getLocation(), 40);
                                world.playSound(boss.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                                world.spawnParticle(Particle.PORTAL, boss.getLocation(), 40);
                                boss.teleport(player.getLocation());
                                ServerUtilities.playBroadcastSound("minecraft:entity.wither_skeleton.step", 1, 0.2f);
                                ServerUtilities.sendBroadcastActionBar(ServerUtilities.getMiniMessage().parse(
                                        "<bold><color:#F9C74F>" + player.getName() +
                                                "</color></bold> <color:#F9844A>ha sido atacado</color>"
                                ));
                            } else {
                                switch (random.nextInt(3)) {
                                    case 1 -> {
                                        TeamsController.getInstance().getPlayingPlayers().forEach(player -> {
                                            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 10, 0));
                                            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 10, 0));
                                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 10, 0));
                                        });
                                        ServerUtilities.playBroadcastSound("minecraft:entity.wither_skeleton.ambient", 1, 0.2f);
                                        ServerUtilities.sendBroadcastActionBar(ServerUtilities.getMiniMessage().parse(
                                                "<color:#43AA8B>Todos fueron maldecidos</color>"
                                        ));
                                    }
                                    case 2 -> {
                                        TeamsController.getInstance().getPlayingPlayers().forEach(player -> {
                                            world.strikeLightning(player.getLocation());
                                            for (int i = 0; i < random.nextInt(2) + 1; ++i) {
                                                WitherSkeleton witherSkeleton =
                                                        summonFirstBossEnemy(world, player.getLocation(), 150f, 0.4f);
                                                ItemStack skeletonHand = new ItemStack(Material.STICK);
                                                skeletonHand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 20);
                                                witherSkeleton.getEquipment().setItemInMainHand(skeletonHand);
                                            }
                                        });
                                        ServerUtilities.playBroadcastSound("minecraft:entity.wither_skeleton.hurt", 1, 0.2f);
                                        ServerUtilities.sendBroadcastActionBar(ServerUtilities.getMiniMessage().parse(
                                                "<color:#F9C74F>Control del ambiente</color>"
                                        ));
                                    }
                                }
                            }
                        } else {
                            if (boss.getEquipment().getItemInMainHand().getType() == Material.NETHERITE_SWORD) {
                                boss.getEquipment().setItemInMainHand(bow);
                                ServerUtilities.playBroadcastSound("minecraft:entity.wither.shoot", 1, 0.2f);
                            } else {
                                boss.getEquipment().setItemInMainHand(sword);
                                ServerUtilities.playBroadcastSound("minecraft:entity.wither.break_block", 1, 0.2f);
                            }
                            ServerUtilities.sendBroadcastActionBar(ServerUtilities.getMiniMessage().parse(
                                    "<color:#FFAFCC>Cambio de armas</color>"
                            ));
                        }
                    }
                }
            }
        }.runTaskTimer(MobAttack.getInstance(), 0L, 20L * 3);

        for (int i = 0; i < 100; ++i) {
            WitherSkeleton witherSkeleton =
                    this.summonFirstBossEnemy(world, this.getRandomLocation(world), 50f, 0.35f);
            ItemStack skeletonHand = new ItemStack(Material.BOW);
            skeletonHand.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 10);
            if (i >= 50) {
                skeletonHand = new ItemStack(Material.IRON_SWORD);
                skeletonHand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
            }
            witherSkeleton.getEquipment().setItemInMainHand(skeletonHand);
        }
    }

    public WitherSkeleton summonFirstBossEnemy(World world, Location location, float maxHealth, float maxSpeed) {
        WitherSkeleton witherSkeleton = (WitherSkeleton) world.spawnEntity(location, EntityType.WITHER_SKELETON);
        witherSkeleton.customName(Component.text("Fantasmita", TextColor.color(0x8EECF5)));
        this.basicEntityConfiguration(witherSkeleton);
        Objects.requireNonNull(witherSkeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(maxHealth);
        Objects.requireNonNull(witherSkeleton.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(maxSpeed);
        Objects.requireNonNull(witherSkeleton.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(20);
        witherSkeleton.setHealth(maxHealth);
        witherSkeleton.getEquipment().setHelmet(null);
        witherSkeleton.getEquipment().setChestplate(null);
        witherSkeleton.getEquipment().setLeggings(null);
        witherSkeleton.getEquipment().setBoots(null);
        return witherSkeleton;
    }

    public void generateWave6(World world) {
        this.currentWaveType = WaveType.NORMAL;
        this.maxEntities = 0;
        for (int i = 0; i < 200; ++i) {
            Creeper creeper = this.summonCreeper("Sustitos", world, this.getRandomLocation(world), 200f, 0.4f);
            creeper.addScoreboardTag(tagInmuneToArrows);
            creeper.addScoreboardTag(tagInmuneToExplosions);
            if (random.nextBoolean()) {
                creeper.setIgnited(true);
            }
            this.waveEntities.add(creeper);
            this.maxEntities++;
        }
    }

    public Creeper summonCreeper(String name, World world, Location location, float maxHealth, float maxSpeed) {
        Creeper creeper = (Creeper) world.spawnEntity(location, EntityType.CREEPER);
        creeper.customName(Component.text(name, TextColor.color(0x98F5E1)));
        this.basicEntityConfiguration(creeper);
        Objects.requireNonNull(creeper.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(maxHealth);
        Objects.requireNonNull(creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(maxSpeed);
        Objects.requireNonNull(creeper.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(10);
        creeper.setHealth(maxHealth);
        return creeper;
    }

    public void generateWave7(World world) {
        this.currentWaveType = WaveType.NORMAL;
        this.maxEntities = 0;
        for (int i = 0; i < 50; ++i) {
            Zombie zombie = (Zombie) world.spawnEntity(this.getRandomLocation(world), EntityType.ZOMBIE);
            this.waveEntities.add(zombie);
            this.maxEntities++;
            zombie.customName(Component.text("Zomwhi", TextColor.color(0x98F5E1)));
            this.basicEntityConfiguration(zombie);
            zombie.setAdult();
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(150f);
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.4f);
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(15);
            zombie.setHealth(150f);
            ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
            helmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
            zombie.getEquipment().setHelmet(helmet);
            ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
            chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
            zombie.getEquipment().setChestplate(chestplate);
            ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
            leggings.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
            zombie.getEquipment().setLeggings(leggings);
            ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
            boots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
            zombie.getEquipment().setBoots(boots);
            ItemStack hand = new ItemStack(Material.NETHERITE_SWORD);
            if (random.nextFloat() >= 0.6) {
                hand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
            } else if (random.nextFloat() >= 0.4) {
                hand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 4);
            } else {
                hand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
            }
            if (random.nextFloat() >= 0.6) {
                hand.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
            }
            zombie.getEquipment().setItemInMainHand(hand);
        }
        for (int i = 0; i < 50; ++i) {
            Skeleton skeleton = (Skeleton) world.spawnEntity(this.getRandomLocation(world), EntityType.SKELETON);
            this.waveEntities.add(skeleton);
            this.maxEntities++;
            skeleton.customName(Component.text("Gorrito", TextColor.color(0x90DBF4)));
            this.basicEntityConfiguration(skeleton);
            Objects.requireNonNull(skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(200f);
            Objects.requireNonNull(skeleton.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.4f);
            Objects.requireNonNull(skeleton.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(15);
            skeleton.setHealth(200f);
            skeleton.addScoreboardTag(tagInmuneToArrows);
            skeleton.addScoreboardTag(tagInmuneToExplosions);
            skeleton.getEquipment().setHelmet(null);
            skeleton.getEquipment().setChestplate(null);
            skeleton.getEquipment().setLeggings(null);
            skeleton.getEquipment().setBoots(null);
            ItemStack hand = new ItemStack(Material.BOW);
            hand.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 20);
            if (random.nextFloat() >= 0.7) {
                hand.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 10);
            }
            skeleton.getEquipment().setItemInMainHand(hand);
        }
        for (int i = 0; i < 50; ++i) {
            Creeper creeper = this.summonCreeper("Sustitos", world, this.getRandomLocation(world), 200f, 0.4f);
            creeper.addScoreboardTag(tagInmuneToExplosions);
            creeper.addScoreboardTag(tagInmuneToArrows);
            this.waveEntities.add(creeper);
            this.maxEntities++;
        }
        for (int i = 0; i < 50; ++i) {
            Creeper creeper = this.summonCreeper("Pum pum", world, this.getRandomLocation(world), 200f, 0.5f);
            this.waveEntities.add(creeper);
            this.maxEntities++;
        }
    }

    public void generateWave8(World world) {
        this.currentWaveType = WaveType.NORMAL;
        this.maxEntities = 0;
        for (int i = 0; i < 100; ++i) {
            Creeper creeper = this.summonCreeper("Pum pum", world, this.getRandomLocation(world), 200f, 0.5f);
            this.waveEntities.add(creeper);
            this.maxEntities++;
        }
        for (int i = 0; i < 100; ++i) {
            Spider spider = (Spider) world.spawnEntity(this.getRandomLocation(world), EntityType.SPIDER);
            this.waveEntities.add(spider);
            this.maxEntities++;
            spider.customName(Component.text("Ara??ita", TextColor.color(0xDAF59B)));
            this.basicEntityConfiguration(spider);
            Objects.requireNonNull(spider.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(200f);
            Objects.requireNonNull(spider.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(15);
            spider.setHealth(200f);
            spider.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 999999, 1));
            spider.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 6));
            spider.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 6));
            spider.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 6));
        }
        for (int i = 0; i < 100; ++i) {
            Zombie zombie = (Zombie) world.spawnEntity(this.getRandomLocation(world), EntityType.ZOMBIE);
            this.waveEntities.add(zombie);
            this.maxEntities++;
            zombie.customName(Component.text("Conter", TextColor.color(0x98F5E1)));
            this.basicEntityConfiguration(zombie);
            zombie.setAdult();
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(200f);
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.4f);
            Objects.requireNonNull(zombie.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(10);
            zombie.setHealth(200f);
            ItemStack hand = new ItemStack(Material.NETHERITE_SWORD);
            hand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
            zombie.getEquipment().setItemInMainHand(hand);
        }
    }

    public void generateWave9(World world) {
        this.currentWaveType = WaveType.NORMAL;
        this.maxEntities = 0;
        for (int i = 0; i < 150; ++i) {
            Evoker evoker = (Evoker) world.spawnEntity(this.getRandomLocation(world), EntityType.EVOKER);
            this.waveEntities.add(evoker);
            this.maxEntities++;
            evoker.customName(Component.text("Capuchita", TextColor.color(0x808CF5)));
            this.basicEntityConfiguration(evoker);
            Objects.requireNonNull(evoker.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(300f);
            Objects.requireNonNull(evoker.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(15);
            evoker.setHealth(300f);
            evoker.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 999999, 1));
            evoker.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 8));
            evoker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 3));
        }
        for (int i = 0; i < 50; ++i) {
            Ravager ravager = (Ravager) world.spawnEntity(this.getRandomLocation(world), EntityType.RAVAGER);
            this.waveEntities.add(ravager);
            this.maxEntities++;
            ravager.customName(Component.text("Guerrero Perdido", TextColor.color(0x808CF5)));
            this.basicEntityConfiguration(ravager);
            Objects.requireNonNull(ravager.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(500f);
            Objects.requireNonNull(ravager.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(15);
            ravager.setHealth(500f);
            ravager.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 3));
            ravager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 3));
        }
    }

}
