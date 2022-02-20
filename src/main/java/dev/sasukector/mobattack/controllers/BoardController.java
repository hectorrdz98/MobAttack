package dev.sasukector.mobattack.controllers;

import dev.sasukector.mobattack.MobAttack;
import dev.sasukector.mobattack.helpers.FastBoard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class BoardController {

    private static BoardController instance = null;
    private final Map<UUID, FastBoard> boards = new HashMap<>();
    private @Setter @Getter boolean hideDays;

    public static BoardController getInstance() {
        if (instance == null) {
            instance = new BoardController();
        }
        return instance;
    }

    public BoardController() {
        Bukkit.getScheduler().runTaskTimer(MobAttack.getInstance(), this::updateBoards, 0L, 20L);
        this.hideDays = false;
    }

    public void newPlayerBoard(Player player) {
        FastBoard board = new FastBoard(player);
        this.boards.put(player.getUniqueId(), board);
    }

    public void removePlayerBoard(Player player) {
        FastBoard board = this.boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }

    public void updateBoards() {
        boards.forEach((uuid, board) -> {
            Player player = Bukkit.getPlayer(uuid);
            assert player != null;

            board.updateTitle("§6§l- Invasión -");

            List<String> lines = new ArrayList<>();
            lines.add("");

            if (TeamsController.getInstance().isPlaying(player)) {
                lines.add("§aEstás vivo...");
            } else if (TeamsController.getInstance().isEliminated(player)) {
                lines.add("§cHas sido eliminado...");
            } else {
                lines.add("§bEres master...");
            }

            lines.add("");
            lines.add("Jugando: §d" + TeamsController.getInstance().getPlayingPlayers().size());
            lines.add("Eliminados: §7" + TeamsController.getInstance().getEliminatedPlayers().size());
            lines.add("");
            lines.add("Oleada actual: §b" + GameController.getInstance().getCurrentRound());
            lines.add("");
            lines.add("Online: §6" + Bukkit.getOnlinePlayers().size());
            if (player.isOp() && !player.getName().equals("Conterstine")) {
                lines.add("TPS: §6" + String.format("%.2f", Bukkit.getTPS()[0]));
            }

            board.updateLines(lines);
        });
    }

}
