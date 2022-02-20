package dev.sasukector.mobattack.commands;

import dev.sasukector.mobattack.controllers.GameController;
import dev.sasukector.mobattack.controllers.TeamsController;
import dev.sasukector.mobattack.helpers.ServerUtilities;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GameCommand implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player && player.isOp()) {
            if (args.length > 0) {
                String option = args[0];
                if (validOptions(player).contains(option)) {
                    switch (option) {
                        case "prepare" -> GameController.getInstance().prepareEvent();
                        case "lobby" -> GameController.getInstance().returnLobby();
                        case "start" -> GameController.getInstance().gameWaitingRound();
                        case "pause" -> GameController.getInstance().gamePausingRound();
                        case "winner" -> GameController.getInstance().gameWinner();
                        case "round" -> {
                            if (args.length > 1) {
                                String roundStr = args[1];
                                if (NumberUtils.isNumber(roundStr)) {
                                    GameController.getInstance().setCurrentRound(Integer.parseInt(roundStr));
                                    player.playSound(player.getLocation(), "minecraft:block.note_block.bell", 1, 1);
                                    ServerUtilities.sendServerMessage(player, ServerUtilities.getMiniMessage().parse(
                                            "Se estableció la ronda como: <bold><color:#2A9D8F>" +
                                                    GameController.getInstance().getCurrentRound() + "</color></bold>"
                                    ));
                                } else {
                                    ServerUtilities.sendServerMessage(player, "§cIndica un número válido");
                                }
                            } else {
                                ServerUtilities.sendServerMessage(player, "§cIndica un número de ronda");
                            }
                        }
                        case "kit" -> GameController.getInstance().givePlayerKit(player);
                        case "kit-all" -> TeamsController.getInstance().getPlayingPlayers()
                                .forEach(alivePlayer -> GameController.getInstance().givePlayerKit(alivePlayer));
                    }
                } else {
                    ServerUtilities.sendServerMessage(player, "§cElige una opción válida");
                }
            } else {
                ServerUtilities.sendServerMessage(player, "§cElige una opción");
            }
        } else if (sender instanceof Player player) {
            ServerUtilities.sendServerMessage(player, "§cPermisos insuficientes");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if(sender instanceof Player player) {
            if (args.length == 1) {
                String partialItem = args[0];
                StringUtil.copyPartialMatches(partialItem, validOptions(player), completions);
            }
        }

        Collections.sort(completions);

        return completions;
    }

    public List<String> validOptions(Player player) {
        List<String> valid = new ArrayList<>();
        if (player.isOp()) {
            valid.add("start");
            valid.add("pause");
            valid.add("winner");
            valid.add("round");
            valid.add("prepare");
            valid.add("lobby");
            valid.add("kit");
            valid.add("kit-all");
        }
        Collections.sort(valid);
        return valid;
    }

}
