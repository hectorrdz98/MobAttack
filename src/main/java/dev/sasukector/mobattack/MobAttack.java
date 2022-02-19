package dev.sasukector.mobattack;

import dev.sasukector.mobattack.commands.*;
import dev.sasukector.mobattack.controllers.BoardController;
import dev.sasukector.mobattack.controllers.TeamsController;
import dev.sasukector.mobattack.events.GameEvents;
import dev.sasukector.mobattack.events.SpawnEvents;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class MobAttack extends JavaPlugin {

    private static @Getter MobAttack instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info(ChatColor.DARK_PURPLE + "MobAttack startup!");
        instance = this;

        // Configuration
        this.saveDefaultConfig();

        // Register events
        this.getServer().getPluginManager().registerEvents(new SpawnEvents(), this);
        this.getServer().getPluginManager().registerEvents(new GameEvents(), this);
        Bukkit.getOnlinePlayers().forEach(player -> BoardController.getInstance().newPlayerBoard(player));

        // Register commands
        Objects.requireNonNull(MobAttack.getInstance().getCommand("conter-tps")).setExecutor(new TPSCommand());
        Objects.requireNonNull(MobAttack.getInstance().getCommand("played")).setExecutor(new PlayedCommand());
        Objects.requireNonNull(MobAttack.getInstance().getCommand("inventory")).setExecutor(new InventoryCommand());
        Objects.requireNonNull(MobAttack.getInstance().getCommand("sit")).setExecutor(new SitCommand());

        // Start teams
        TeamsController.getInstance();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info(ChatColor.DARK_PURPLE + "MobAttack shutdown!");
    }
}
