package dev.sasukector.hundreddaysborder.controllers;

import com.google.common.util.concurrent.AtomicDouble;
import dev.sasukector.hundreddaysborder.HundredDaysBorder;
import dev.sasukector.hundreddaysborder.helpers.ServerUtilities;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

public class GameController {

    private static GameController instance = null;
    private @Getter long lastDay = 0;
    private @Getter int schedulerID = -1;

    public static void getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
    }

    public GameController() {
        World overworld = ServerUtilities.getWorld("overworld");
        if (overworld != null) {
            this.lastDay = (overworld.getFullTime() / 24000);
        }

        this.startScheduler();
    }

    public void expandBorder() {
        AtomicDouble newWorldBorder = new AtomicDouble(0);
        World overworld = ServerUtilities.getWorld("overworld");
        if (overworld != null) {
            newWorldBorder.set(overworld.getWorldBorder().getSize() + (16 * 2));
            ServerUtilities.playBroadcastSound("minecraft:ui.loom.take_result", 1f, 0.3f);
            ServerUtilities.sendBroadcastMessage(ServerUtilities.getMiniMessage().parse(
                    "<color:#F75291>Nuevo día, nuevo borde. Ahora el borde mide: </color>" +
                            "<bold><color:#8877AC>" + newWorldBorder.get() + "</color></bold>"
            ));
        } else {
            ServerUtilities.sendBroadcastMessage(ServerUtilities.getMiniMessage().parse(
                    "<color:#F63D43>Ocurrió un error al expandir el borde</color>"
            ));
        }

        List<World> worlds = List.of(
                Objects.requireNonNull(ServerUtilities.getWorld("overworld")),
                Objects.requireNonNull(ServerUtilities.getWorld("nether")),
                Objects.requireNonNull(ServerUtilities.getWorld("end"))
        );
        worlds.forEach(world -> {
            if (world != null && newWorldBorder.get() != 0) {
                world.getWorldBorder().setSize(newWorldBorder.get());
            }
        });
    }

    public void startScheduler() {
        this.schedulerID = new BukkitRunnable() {
            @Override
            public void run() {
                World overworld = ServerUtilities.getWorld("overworld");
                if (overworld != null) {
                    long currentDay = overworld.getFullTime() / 24000;
                    if (currentDay != lastDay) {
                        lastDay = currentDay;
                        expandBorder();
                    }
                }
            }
        }.runTaskTimer(HundredDaysBorder.getInstance(), 0L, 30 * 20L).getTaskId();
    }


}
