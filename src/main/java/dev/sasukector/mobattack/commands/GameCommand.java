package dev.sasukector.mobattack.commands;

import dev.sasukector.mobattack.controllers.GameController;
import dev.sasukector.mobattack.helpers.ServerUtilities;
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
                        case "prepare" -> {
                            player.playSound(player.getLocation(), "minecraft:block.note_block.bell", 1, 1);
                            ServerUtilities.sendServerMessage(player, ServerUtilities.getMiniMessage().parse(
                                    "El estatus del juego ahora es: <bold><color:#2A9D8F>PREPARANDO</color></bold>"
                            ));
                            GameController.getInstance().prepareEvent();
                        }
                        case "lobby" -> {
                            player.playSound(player.getLocation(), "minecraft:block.note_block.bell", 1, 1);
                            ServerUtilities.sendServerMessage(player, ServerUtilities.getMiniMessage().parse(
                                    "El estatus del juego ahora es: <bold><color:#2A9D8F>LOBBY</color></bold>"
                            ));
                            GameController.getInstance().returnLobby();
                        }
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
        }
        Collections.sort(valid);
        return valid;
    }

}
