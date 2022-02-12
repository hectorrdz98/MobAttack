package dev.sasukector.hundreddaysants;

import dev.sasukector.hundreddaysants.commands.*;
import dev.sasukector.hundreddaysants.controllers.BoardController;
import dev.sasukector.hundreddaysants.controllers.TeamsController;
import dev.sasukector.hundreddaysants.events.GameEvents;
import dev.sasukector.hundreddaysants.events.SpawnEvents;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class HundredDaysAnts extends JavaPlugin {

    private static @Getter HundredDaysAnts instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info(ChatColor.DARK_PURPLE + "HundredDaysAnts startup!");
        instance = this;

        // Configuration
        this.saveDefaultConfig();

        // Register events
        this.getServer().getPluginManager().registerEvents(new SpawnEvents(), this);
        this.getServer().getPluginManager().registerEvents(new GameEvents(), this);
        Bukkit.getOnlinePlayers().forEach(player -> BoardController.getInstance().newPlayerBoard(player));

        // Register commands
        Objects.requireNonNull(HundredDaysAnts.getInstance().getCommand("conter-tps")).setExecutor(new TPSCommand());
        Objects.requireNonNull(HundredDaysAnts.getInstance().getCommand("played")).setExecutor(new PlayedCommand());
        Objects.requireNonNull(HundredDaysAnts.getInstance().getCommand("toggleDays")).setExecutor(new ToggleDaysCommand());
        Objects.requireNonNull(HundredDaysAnts.getInstance().getCommand("inventory")).setExecutor(new InventoryCommand());
        Objects.requireNonNull(HundredDaysAnts.getInstance().getCommand("sit")).setExecutor(new SitCommand());
        Objects.requireNonNull(HundredDaysAnts.getInstance().getCommand("hormiguero")).setExecutor(new HormigueroCommand());

        // Start teams
        TeamsController.getInstance();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info(ChatColor.DARK_PURPLE + "HundredDaysAnts shutdown!");
    }
}
