package dev.sasukector.hundreddaysants.events;

import dev.sasukector.hundreddaysants.controllers.HormiguerosController;
import dev.sasukector.hundreddaysants.helpers.ServerUtilities;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class GameEvents implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (event.isCancelled() || event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
            return;
        }

        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player) return;

        int netherFort = 300;
        int bastion = 499;
        int end = 701;
        int endCity = 898;

        Location location = event.getLocation();
        double z = location.getZ();

        Entity entity = null;

        if (z >= netherFort - 10 && z <= netherFort + 10) {
            if (random.nextFloat() <= 0.7) {
                entity = location.getWorld().spawnEntity(location, EntityType.ZOMBIFIED_PIGLIN, CreatureSpawnEvent.SpawnReason.CUSTOM);
            } else {
                switch (random.nextInt(4)) {
                    case 0 -> entity = location.getWorld().spawnEntity(location, EntityType.BLAZE, CreatureSpawnEvent.SpawnReason.CUSTOM);
                    case 1 -> entity = location.getWorld().spawnEntity(location, EntityType.WITHER_SKELETON, CreatureSpawnEvent.SpawnReason.CUSTOM);
                    case 2 -> entity = location.getWorld().spawnEntity(location, EntityType.MAGMA_CUBE, CreatureSpawnEvent.SpawnReason.CUSTOM);
                    case 3 -> entity = location.getWorld().spawnEntity(location, EntityType.SKELETON, CreatureSpawnEvent.SpawnReason.CUSTOM);
                }
            }
        } else if (z >= bastion - 10 && z <= bastion + 10) {
            if (random.nextFloat() <= 0.4) {
                entity = location.getWorld().spawnEntity(location, EntityType.ZOMBIFIED_PIGLIN, CreatureSpawnEvent.SpawnReason.CUSTOM);
            } else {
                switch (random.nextInt(5)) {
                    case 0 -> {
                        entity = location.getWorld().spawnEntity(location, EntityType.PIGLIN, CreatureSpawnEvent.SpawnReason.CUSTOM);
                        Piglin piglin = (Piglin) entity;
                        piglin.setImmuneToZombification(true);
                    }
                    case 1 -> {
                        entity = location.getWorld().spawnEntity(location, EntityType.PIGLIN_BRUTE, CreatureSpawnEvent.SpawnReason.CUSTOM);
                        PiglinBrute piglinBrute = (PiglinBrute) entity;
                        piglinBrute.setImmuneToZombification(true);
                    }
                    case 2 -> {
                        entity = location.getWorld().spawnEntity(location, EntityType.HOGLIN, CreatureSpawnEvent.SpawnReason.CUSTOM);
                        Hoglin hoglin = (Hoglin) entity;
                        hoglin.setImmuneToZombification(true);
                    }
                    case 3 -> entity = location.getWorld().spawnEntity(location, EntityType.MAGMA_CUBE, CreatureSpawnEvent.SpawnReason.CUSTOM);
                    case 4 -> entity = location.getWorld().spawnEntity(location, EntityType.SKELETON, CreatureSpawnEvent.SpawnReason.CUSTOM);
                }
            }
        } else if (z >= end - 10 && z <= end + 10) {
            if (random.nextFloat() <= 0.99) {
                entity = location.getWorld().spawnEntity(location, EntityType.ENDERMAN, CreatureSpawnEvent.SpawnReason.CUSTOM);
            } else {
                HormiguerosController.getInstance().spawnEndBoss();
            }
        } else if (z >= endCity - 10 && z <= endCity + 10) {
            if (random.nextFloat() <= 0.8) {
                entity = location.getWorld().spawnEntity(location, EntityType.ENDERMAN, CreatureSpawnEvent.SpawnReason.CUSTOM);
            } else {
                entity = location.getWorld().spawnEntity(location, EntityType.SHULKER, CreatureSpawnEvent.SpawnReason.CUSTOM);
            }
        }

        if (entity != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof WitherSkeleton witherSkeleton) {
            if (witherSkeleton.getScoreboardTags().contains("dragon_child")) {
                ServerUtilities.sendBroadcastMessage(ServerUtilities.getMiniMessage().parse(
                        "<color:#FFFFFF>Murió el </color><bold><color:#F72585>Hijo de la Dragona</color></bold>"
                ));
                ServerUtilities.playBroadcastSound("minecraft:entity.ender_dragon.death", 1f, 2f);
                event.getDrops().add(new ItemStack(Material.END_STONE, random.nextInt(33) + 32));
                if (random.nextFloat() >= 0.9) {
                    ServerUtilities.sendBroadcastMessage(ServerUtilities.getMiniMessage().parse(
                            "<color:#FFFFFF>Loot especial: </color><bold><color:#4CC9F0>Elytra</color></bold>"
                    ));
                    event.getDrops().add(new ItemStack(Material.ELYTRA, 1));
                } else if (random.nextFloat() >= 0.6) {
                    int amount = random.nextInt(9) + 3;
                    ServerUtilities.sendBroadcastMessage(ServerUtilities.getMiniMessage().parse(
                            "<color:#FFFFFF>Loot especial: </color><bold><color:#4CC9F0>" + amount + " Diamantes</color></bold>"
                    ));
                    event.getDrops().add(new ItemStack(Material.DIAMOND, amount));
                } else if (random.nextFloat() >= 0.8) {
                    int amount = random.nextInt(3) + 1;
                    ServerUtilities.sendBroadcastMessage(ServerUtilities.getMiniMessage().parse(
                            "<color:#FFFFFF>Loot especial: </color><bold><color:#4CC9F0>" + amount + " Netherite</color></bold>"
                    ));
                    event.getDrops().add(new ItemStack(Material.NETHERITE_INGOT, amount));
                }  else if (random.nextFloat() >= 0.5) {
                    int amount = random.nextInt(12) + 3;
                    ServerUtilities.sendBroadcastMessage(ServerUtilities.getMiniMessage().parse(
                            "<color:#FFFFFF>Loot especial: </color><bold><color:#4CC9F0>" + amount + " Oro</color></bold>"
                    ));
                    event.getDrops().add(new ItemStack(Material.GOLD_INGOT, amount));
                } else {
                    ServerUtilities.sendBroadcastMessage(ServerUtilities.getMiniMessage().parse(
                            "<color:#FFFFFF>Mala suerte, no hubo loot especial</bold>"
                    ));
                }
            }
        }
    }

}
