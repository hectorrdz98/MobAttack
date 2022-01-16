package dev.sasukector.hundreddaysborder.controllers;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamsController {

    private static TeamsController instance = null;
    private @Getter Team masterTeam;

    public static TeamsController getInstance() {
        if (instance == null) {
            instance = new TeamsController();
        }
        return instance;
    }

    public TeamsController() {
        this.createOrLoadTeams();
    }

    public void createOrLoadTeams() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        masterTeam = scoreboard.getTeam("master");

        if (masterTeam == null) {
            masterTeam = scoreboard.registerNewTeam("master");
            masterTeam.color(NamedTextColor.AQUA);
            masterTeam.prefix(Component.text("♔ "));
            masterTeam.setAllowFriendlyFire(false);
        }
    }

}
