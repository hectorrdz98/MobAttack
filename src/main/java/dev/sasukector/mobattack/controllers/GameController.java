package dev.sasukector.mobattack.controllers;

import dev.sasukector.mobattack.MobAttack;
import dev.sasukector.mobattack.helpers.ServerUtilities;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
    private @Getter boolean pvpEnabled;

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
        this.pvpEnabled = false;
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

    public void givePlayerKit(Player player) {
        player.getInventory().clear();

        ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
        helmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        helmet.addUnsafeEnchantment(Enchantment.MENDING, 1);
        helmet.addUnsafeEnchantment(Enchantment.OXYGEN, 3);
        helmet.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
        player.getInventory().setHelmet(helmet);

        ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        chestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        chestplate.addUnsafeEnchantment(Enchantment.MENDING, 1);
        player.getInventory().setChestplate(chestplate);

        ItemStack leggings = new ItemStack(Material.NETHERITE_LEGGINGS);
        leggings.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        leggings.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        leggings.addUnsafeEnchantment(Enchantment.MENDING, 1);
        player.getInventory().setLeggings(leggings);

        ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        boots.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        boots.addUnsafeEnchantment(Enchantment.MENDING, 1);
        boots.addUnsafeEnchantment(Enchantment.SOUL_SPEED, 3);
        boots.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 3);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 4);
        player.getInventory().setBoots(boots);

        player.getInventory().setItemInOffHand(new ItemStack(Material.TOTEM_OF_UNDYING));

        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
        sword.addUnsafeEnchantment(Enchantment.SWEEPING_EDGE, 3);
        sword.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        sword.addUnsafeEnchantment(Enchantment.MENDING, 1);
        sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
        player.getInventory().addItem(sword);

        ItemStack sword2 = new ItemStack(Material.NETHERITE_SWORD);
        sword2.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 5);
        sword2.addUnsafeEnchantment(Enchantment.SWEEPING_EDGE, 3);
        sword2.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        sword2.addUnsafeEnchantment(Enchantment.MENDING, 1);
        sword2.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
        player.getInventory().setItem(27, sword2);

        ItemStack bow = new ItemStack(Material.BOW);
        bow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 5);
        bow.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        bow.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().addItem(bow);

        ItemStack bow2 = new ItemStack(Material.BOW);
        bow2.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 5);
        bow2.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
        bow2.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow2.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        bow2.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().setItem(28, bow2);

        player.getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING));
        player.getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING));
        player.getInventory().addItem(new ItemStack(Material.SHIELD));
        player.getInventory().addItem(new ItemStack(Material.EXPERIENCE_BOTTLE, 64));
        player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 64));
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, 64));
        player.getInventory().addItem(new ItemStack(Material.ARROW, 64));

        player.getInventory().setItem(11, new ItemStack(Material.TOTEM_OF_UNDYING));
        player.getInventory().setItem(20, new ItemStack(Material.TOTEM_OF_UNDYING));
        player.getInventory().setItem(29, new ItemStack(Material.TOTEM_OF_UNDYING));
        player.getInventory().setItem(12, new ItemStack(Material.TOTEM_OF_UNDYING));
        player.getInventory().setItem(21, new ItemStack(Material.TOTEM_OF_UNDYING));
        player.getInventory().setItem(30, new ItemStack(Material.TOTEM_OF_UNDYING));

        player.getInventory().setItem(13, new ItemStack(Material.SHIELD));
        player.getInventory().setItem(22, new ItemStack(Material.SHIELD));
        player.getInventory().setItem(31, new ItemStack(Material.SHIELD));

        player.getInventory().setItem(14, new ItemStack(Material.EXPERIENCE_BOTTLE, 64));
        player.getInventory().setItem(23, new ItemStack(Material.EXPERIENCE_BOTTLE, 64));
        player.getInventory().setItem(32, new ItemStack(Material.EXPERIENCE_BOTTLE, 64));

        player.getInventory().setItem(15, new ItemStack(Material.ENDER_PEARL, 16));
        player.getInventory().setItem(24, new ItemStack(Material.ENDER_PEARL, 16));

        player.getInventory().setItem(33, new ItemStack(Material.WATER_BUCKET));
        player.getInventory().setItem(35, new ItemStack(Material.GOLDEN_CARROT, 64));

        player.updateInventory();
    }

    public void dropPlayer(Player player) {
        for (ItemStack itemStack : player.getInventory()) {
            if (itemStack != null) {
                player.getWorld().dropItem(player.getLocation(), itemStack);
            }
        }
    }

    public void restartPlayer(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(10);
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
        BossBarController.getInstance().stopCurrentBossBar();
        this.currentRound = 0;
        this.currentStatus = Status.LOBBY;
        this.pvpEnabled = false;
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
        BossBarController.getInstance().createTimerBossBar(
                60 * 10,
                "gameWaitingRound",
                "Equipando",
                BarColor.YELLOW
        );
    }

    public void gameWaitingRound() {
        this.currentRound++;
        this.currentStatus = Status.WAITING;
        this.pvpEnabled = false;
        this.buildWall(Material.BROWN_STAINED_GLASS);
        this.teleportAllPlayingPlayers(this.arenaArea);
        WaveController.getInstance().deleteItemsOnGround();
        WaveController.getInstance().createWave();
        if (WaveController.getInstance().getCurrentWaveType() == WaveController.WaveType.NORMAL) {
            ServerUtilities.sendBroadcastAnnounce(
                    ServerUtilities.getMiniMessage().parse(
                            "<bold><gradient:#5C4D7D:#B7094C>Oleada #" + this.currentRound + "</gradient></bold>"
                    ),
                    ServerUtilities.getMiniMessage().parse(
                            "Alista tu inventario, en <bold><color:#FB8500>30 segundos</color></bold> llegarán los mobs..."
                    )
            );
        } else {
            ServerUtilities.sendBroadcastAnnounce(
                    ServerUtilities.getMiniMessage().parse(
                            "<bold><gradient:#5C4D7D:#B7094C>Oleada #" + this.currentRound + " - BOSS -</gradient></bold>"
                    ),
                    ServerUtilities.getMiniMessage().parse(
                            "Esta es una oleada de jefe, en <bold><color:#FB8500>30 segundos</color></bold> tendrán " +
                                    "que matarlo para pasar a la siguiente ronda..."
                    )
            );
        }
        BossBarController.getInstance().createTimerBossBar(
                30,
                "gameStartingRound",
                "Iniciando ronda",
                BarColor.YELLOW
        );
    }

    public void gameStartingRound() {
        BossBarController.getInstance().createWaveBossBar();
        this.currentStatus = Status.PLAYING;
        this.buildWall(Material.AIR);
        ServerUtilities.sendBroadcastTitle(
                Component.text("Sobrevive...", TextColor.color(0x0096C7)),
                Component.empty()
        );
        ServerUtilities.playBroadcastSound("minecraft:music.effects.item", 1, 1);
    }

    public void gamePausingRound() {
        WaveController.getInstance().deleteWave();
        if (WaveController.getInstance().getMaxWaves() > this.currentRound) {
            ServerUtilities.sendBroadcastTitle(
                    Component.text("Ha terminado", TextColor.color(0x0096C7)),
                    Component.text("la ronda...", TextColor.color(0x48CAE4))
            );
            ServerUtilities.sendBroadcastAnnounce(
                    ServerUtilities.getMiniMessage().parse(
                            "<bold><gradient:#5C4D7D:#B7094C>Oleada #" + this.currentRound + " completada</gradient></bold>"
                    ),
                    ServerUtilities.getMiniMessage().parse(
                            "¡Felicidades! Has completado la oleada, tienes <bold><color:#FB8500>30 segundos</color></bold> " +
                                    "para agarrar lo que ocupes del suelo, en lo que se prepara la siguiente oleada."
                    )
            );
            BossBarController.getInstance().createTimerBossBar(
                    30,
                    "gameWaitingRound",
                    "Terminando ronda",
                    BarColor.BLUE
            );
        } else {
            this.gameWinner();
        }
    }

    public void gameWinner() {
        this.currentStatus = Status.WAITING;
        WaveController.getInstance().deleteWave();
        int alivePlayers = TeamsController.getInstance().getPlayingPlayers().size();
        if (alivePlayers > 1) {
            ServerUtilities.playBroadcastSound("minecraft:music.effects.board", 1, 1);
            ServerUtilities.sendBroadcastAnnounce(
                    ServerUtilities.getMiniMessage().parse(
                            "<bold><gradient:#5C4D7D:#B7094C>Duelo a muerte</gradient></bold>"
                    ),
                    ServerUtilities.getMiniMessage().parse(
                            "¡Felicidades! Ustedes sobrevivieron a las oleadas. Pero solo puede haber un ganador... " +
                                    "En <bold><color:#FB8500>1 minuto</color></bold> tendrán un duelo a muerte hasta " +
                                    "conseguir al ganador. Mucha suerte..."
                    )
            );
            BossBarController.getInstance().createTimerBossBar(
                    60,
                    "gameLastPvP",
                    "PvP iniciando",
                    BarColor.PINK
            );
        } else {
            String message;
            if (alivePlayers == 1) {
                Player player = TeamsController.getInstance().getPlayingPlayers().get(0);
                message = "¡Felicidades <bold><color:#FB8500>" + player.getName() + "</color></bold> has demostrado ser mejor " +
                        "que la bola de novatos que murieron ¡Buen trabajo!";
            } else {
                message = "¿WTF? ¿No sobrevivió nadie...? Que lamentable... Ni modo, tocó acabar la partida";
            }
            ServerUtilities.playBroadcastSound("minecraft:music.effects.board", 1, 1);
            ServerUtilities.sendBroadcastAnnounce(null, ServerUtilities.getMiniMessage().parse(message));
            BossBarController.getInstance().createTimerBossBar(
                    30,
                    "returnLobby",
                    "Finalizando partida",
                    BarColor.RED
            );
        }
    }

    public void checkPossibleWaveWin() {
        if (WaveController.getInstance().getWaveEntities().size() == 0) {
            this.gamePausingRound();
        }
    }

    public void gameLastPvP() {
        BossBarController.getInstance().stopCurrentBossBar();
        this.pvpEnabled = true;
        this.currentStatus = Status.PLAYING;
        ServerUtilities.playBroadcastSound("minecraft:music.effects.board", 1, 1);
        ServerUtilities.sendBroadcastTitle(
                Component.text("PvP", TextColor.color(0x0096C7)),
                Component.text("activado", TextColor.color(0x48CAE4))
        );
    }

}
