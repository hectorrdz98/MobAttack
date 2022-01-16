package dev.sasukector.hundreddaysborder;

import dev.sasukector.hundreddaysborder.commands.InventoryCommand;
import dev.sasukector.hundreddaysborder.commands.PlayedCommand;
import dev.sasukector.hundreddaysborder.commands.TPSCommand;
import dev.sasukector.hundreddaysborder.commands.ToggleDaysCommand;
import dev.sasukector.hundreddaysborder.controllers.BoardController;
import dev.sasukector.hundreddaysborder.controllers.GameController;
import dev.sasukector.hundreddaysborder.controllers.TeamsController;
import dev.sasukector.hundreddaysborder.events.SpawnEvents;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class HundredDaysBorder extends JavaPlugin {

    private static @Getter HundredDaysBorder instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info(ChatColor.DARK_PURPLE + "HundredDaysBorder startup!");
        instance = this;

        // Configuration
        this.saveDefaultConfig();

        // Register events
        this.getServer().getPluginManager().registerEvents(new SpawnEvents(), this);
        Bukkit.getOnlinePlayers().forEach(player -> BoardController.getInstance().newPlayerBoard(player));

        // Register commands
        Objects.requireNonNull(HundredDaysBorder.getInstance().getCommand("conter-tps")).setExecutor(new TPSCommand());
        Objects.requireNonNull(HundredDaysBorder.getInstance().getCommand("played")).setExecutor(new PlayedCommand());
        Objects.requireNonNull(HundredDaysBorder.getInstance().getCommand("toggleDays")).setExecutor(new ToggleDaysCommand());
        Objects.requireNonNull(HundredDaysBorder.getInstance().getCommand("inventory")).setExecutor(new InventoryCommand());

        // Start teams
        TeamsController.getInstance();

        // Start game
        GameController.getInstance();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info(ChatColor.DARK_PURPLE + "HundredDaysBorder shutdown!");
    }
}
