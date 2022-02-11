package dev.sasukector.hundreddaysants.commands;

import dev.sasukector.hundreddaysants.HundredDaysAnts;
import dev.sasukector.hundreddaysants.controllers.HormiguerosController;
import dev.sasukector.hundreddaysants.helpers.ServerUtilities;
import dev.sasukector.hundreddaysants.models.Hormiguero;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class HormigueroCommand implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                String hormigueroName = args[0];
                Hormiguero hormiguero = HormiguerosController.getInstance().getHormiguero(hormigueroName);
                if (hormiguero != null && validOptions().contains(hormigueroName)) {
                    Location location = hormiguero.location();
                    if (location != null) {
                        player.teleport(location);
                        player.sendActionBar(ServerUtilities.getMiniMessage().parse(
                                "<color:#FFFFFF>Hormiguero [</color><bold><color:#B7094C>" +
                                        hormigueroName + "</color></bold><color:#FFFFFF>]</color>"
                        ));
                        Bukkit.getScheduler().runTaskLater(HundredDaysAnts.getInstance(), () ->
                                player.playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1),
                                10L);
                        if (!HormiguerosController.getInstance().isRestartBoss() && hormiguero.name().equals("end")) {
                            HormiguerosController.getInstance().spawnEndBoss();
                            HormiguerosController.getInstance().setRestartBoss(true);
                        }
                    } else {
                        ServerUtilities.sendServerMessage(player, "§cNo se pudo ir al hormiguero");
                    }
                } else {
                    ServerUtilities.sendServerMessage(player, "§cHormiguero no válido");
                }
            } else {
                ServerUtilities.sendServerMessage(player, "§cIndica el nombre del hormiguero");
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if(sender instanceof Player) {
            if (args.length == 1) {
                String partialItem = args[0];
                StringUtil.copyPartialMatches(partialItem, validOptions(), completions);
            }
        }

        Collections.sort(completions);

        return completions;
    }

    public List<String> validOptions() {
        return HormiguerosController.getInstance().getValidHormigueros()
                .stream().map(Hormiguero::name).collect(Collectors.toList());
    }

}
