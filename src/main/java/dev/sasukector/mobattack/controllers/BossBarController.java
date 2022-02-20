package dev.sasukector.mobattack.controllers;

import dev.sasukector.mobattack.MobAttack;
import dev.sasukector.mobattack.helpers.ServerUtilities;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

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

    public void createTimerBossBar(int time) {
        this.remainingTime = time;
        currentBossBar.setColor(BarColor.YELLOW);
        currentBossBar.setStyle(BarStyle.SEGMENTED_10);
        currentBossBar.setProgress(1);
        currentBossBar.setVisible(true);
        Bukkit.getOnlinePlayers().forEach(player -> currentBossBar.addPlayer(player));
        this.taskID = new BukkitRunnable() {
            @Override
            public void run() {
                remainingTime--;
                currentBossBar.setTitle("Preparación: §e" + getTimer());
                currentBossBar.setProgress(remainingTime / (double) time);
                if (remainingTime <= 0) {
                    GameController.getInstance().gameStartedEvent();
                } else if (remainingTime <= 10 || remainingTime % 60 == 0) {
                    ServerUtilities.playBroadcastSound("minecraft:block.note_block.xylophone", 1, 1);
                }
            }
        }.runTaskTimer(MobAttack.getInstance(), 0L, 20L).getTaskId();
    }

    public String getTimer() {
        return (this.remainingTime / 60) + ":" + String.format("%02d", (this.remainingTime % 60)) + " m";
    }

}
