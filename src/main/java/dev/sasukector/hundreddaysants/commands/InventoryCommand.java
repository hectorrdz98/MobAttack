package dev.sasukector.hundreddaysants.commands;

import dev.sasukector.hundreddaysants.helpers.ServerUtilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryCommand implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player && player.isOp()) {
            if (args.length > 0) {
                String playerName = args[0];
                Player searchedPlayer = Bukkit.getPlayer(playerName);
                if (searchedPlayer != null && validOptions().contains(playerName)) {
                    player.playSound(player.getLocation(), "minecraft:block.note_block.bell", 1, 1);
                    Inventory inventory = Bukkit.createInventory(null, 45, Component.text(
                            "Inventario de " + searchedPlayer.getName(), TextColor.color(0xB7094C)
                    ));

                    ItemStack marker = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                    ItemMeta markerMeta = marker.getItemMeta();
                    markerMeta.displayName(Component.empty());
                    markerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    marker.setItemMeta(markerMeta);

                    inventory.setItem(0, marker.clone());
                    inventory.setItem(1, searchedPlayer.getEquipment().getHelmet());
                    inventory.setItem(2, searchedPlayer.getEquipment().getChestplate());
                    inventory.setItem(3, searchedPlayer.getEquipment().getLeggings());
                    inventory.setItem(4, searchedPlayer.getEquipment().getBoots());
                    inventory.setItem(5, marker.clone());
                    inventory.setItem(6, marker.clone());
                    inventory.setItem(7, searchedPlayer.getEquipment().getItemInOffHand());
                    inventory.setItem(8, marker.clone());

                    for (int i = 9; i < 45; ++i) {
                        inventory.setItem(i, searchedPlayer.getInventory().getItem(i - 9));
                    }

                    player.openInventory(inventory);
                } else {
                    ServerUtilities.sendServerMessage(player, "§cJugador no válido");
                }
            } else {
                ServerUtilities.sendServerMessage(player, "§cIndica el nombre del jugador");
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
        return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
    }

}
