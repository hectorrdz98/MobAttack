package dev.sasukector.mobattack.controllers;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamsController {

    private static TeamsController instance = null;
    private @Getter Team masterTeam;
    private @Getter Team playingTeam;
    private @Getter Team eliminatedTeam;

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
        playingTeam = scoreboard.getTeam("playing");
        eliminatedTeam = scoreboard.getTeam("eliminated");

        if (masterTeam == null) {
            masterTeam = scoreboard.registerNewTeam("master");
            masterTeam.color(NamedTextColor.AQUA);
            masterTeam.prefix(Component.text("♔ "));
            masterTeam.setAllowFriendlyFire(false);
        }
        if (playingTeam == null) {
            playingTeam = scoreboard.registerNewTeam("playing");
            playingTeam.color(NamedTextColor.LIGHT_PURPLE);
            playingTeam.prefix(Component.text("⚜ "));
        }
        if (eliminatedTeam == null) {
            eliminatedTeam = scoreboard.registerNewTeam("eliminated");
            eliminatedTeam.color(NamedTextColor.GRAY);
            eliminatedTeam.prefix(Component.text("☠ "));
        }
    }

    public List<Player> getPlayingPlayers() {
        List<Player> players = new ArrayList<>();
        this.playingTeam.getEntries().forEach(entry -> {
            Player player = Bukkit.getPlayer(entry);
            if (player != null) {
                players.add(player);
            }
        });
        return players;
    }

    public boolean isFinalist(Player player) {
        return this.getPlayingPlayers().contains(player);
    }

    public List<Player> getEliminatedPlayers() {
        List<Player> players = new ArrayList<>();
        this.eliminatedTeam.getEntries().forEach(entry -> {
            Player player = Bukkit.getPlayer(entry);
            if (player != null) {
                players.add(player);
            }
        });
        return players;
    }

    public boolean isEliminated(Player player) {
        return this.getEliminatedPlayers().contains(player);
    }

}
