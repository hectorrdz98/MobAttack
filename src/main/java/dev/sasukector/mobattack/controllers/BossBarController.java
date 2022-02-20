package dev.sasukector.mobattack.controllers;

import dev.sasukector.mobattack.MobAttack;
import dev.sasukector.mobattack.helpers.ServerUtilities;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Method;
import java.util.Objects;

public class BossBarController {

    private static BossBarController instance = null;
    private @Getter BossBar currentBossBar = null;
    private int remainingTime = 0;
    private @Getter @Setter int taskID = -1;

    public static BossBarController getInstance() {
        if (instance == null) {
            instance = new BossBarController();
        }
        return instance;
    }

    public BossBarController() {
        NamespacedKey bossBarNamespacedKey = new NamespacedKey(MobAttack.getInstance(), "bossBarMobAttack");
        this.stopCurrentBossBar();
        if (this.currentBossBar == null) {
            this.currentBossBar = Bukkit.getServer().getBossBar(bossBarNamespacedKey);
            if (this.currentBossBar == null) {
                this.currentBossBar = Bukkit.createBossBar(bossBarNamespacedKey, "BossBar", BarColor.WHITE, BarStyle.SEGMENTED_10);
            }
            this.currentBossBar.setVisible(false);
        }
    }

    public void stopCurrentBossBar() {
        if (this.currentBossBar != null) {
            this.currentBossBar.removeAll();
            currentBossBar.setVisible(false);
        }
        if (this.taskID != -1) {
            Bukkit.getScheduler().cancelTask(this.taskID);
            this.taskID = -1;
        }
    }

    public void createTimerBossBar(int time, String redirectMethod, String title, BarColor color) {
        this.stopCurrentBossBar();
        this.remainingTime = time;
        currentBossBar.setColor(color);
        currentBossBar.setStyle(BarStyle.SEGMENTED_10);
        currentBossBar.setProgress(1);
        currentBossBar.setVisible(true);
        Bukkit.getOnlinePlayers().forEach(player -> currentBossBar.addPlayer(player));
        this.taskID = new BukkitRunnable() {
            @Override
            public void run() {
                remainingTime--;
                currentBossBar.setTitle(title + ": §e" + getTimer());
                currentBossBar.setProgress(remainingTime / (double) time);
                if (remainingTime <= 0) {
                    try {
                        Method method = GameController.getInstance().getClass().getDeclaredMethod(redirectMethod);
                        method.invoke(GameController.getInstance());
                    } catch (Exception e) {
                        Bukkit.getLogger().info("§cCan't execute redirect method of createTimerBossBar()§r");
                    }
                } else if (remainingTime <= 5 || remainingTime % 60 == 0) {
                    ServerUtilities.playBroadcastSound("minecraft:block.note_block.xylophone", 1, 1);
                }
            }
        }.runTaskTimer(MobAttack.getInstance(), 0L, 20L).getTaskId();
    }

    public String getTimer() {
        return (this.remainingTime / 60) + ":" + String.format("%02d", (this.remainingTime % 60)) + " m";
    }

    public void createWaveBossBar() {
        this.stopCurrentBossBar();
        if (WaveController.getInstance().getCurrentWaveType() == WaveController.WaveType.NORMAL) {
            currentBossBar.setColor(BarColor.YELLOW);
            currentBossBar.setStyle(BarStyle.SOLID);
            currentBossBar.setProgress(1);
            currentBossBar.setVisible(true);
            Bukkit.getOnlinePlayers().forEach(player -> currentBossBar.addPlayer(player));
            this.taskID = new BukkitRunnable() {
                @Override
                public void run() {
                    currentBossBar.setTitle("Quedan §e" + WaveController.getInstance().getWaveEntities().size() + " enemigos");
                    int current = WaveController.getInstance().getWaveEntities().size();
                    if (current != 0) {
                        currentBossBar.setProgress(current / (double) WaveController.getInstance().getMaxEntities());
                    } else {
                        currentBossBar.setProgress(0);
                    }
                }
            }.runTaskTimer(MobAttack.getInstance(), 0L, 20L).getTaskId();
        } else {
            currentBossBar.setColor(BarColor.RED);
            currentBossBar.setStyle(BarStyle.SEGMENTED_20);
            currentBossBar.setProgress(1);
            currentBossBar.setVisible(true);
            Bukkit.getOnlinePlayers().forEach(player -> currentBossBar.addPlayer(player));
            this.taskID = new BukkitRunnable() {
                @Override
                public void run() {
                    LivingEntity boss = WaveController.getInstance().getWaveEntities().get(0);
                    double currentHealth = boss.getHealth();
                    double maxHealth = Objects.requireNonNull(boss.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();
                    currentBossBar.setTitle("Vida [§e" + Math.round(currentHealth) +
                            "§r, §c" +Math.round(maxHealth) + "§r]");
                    currentBossBar.setProgress(currentHealth / maxHealth);
                }
            }.runTaskTimer(MobAttack.getInstance(), 0L, 20L).getTaskId();
        }
    }

}
