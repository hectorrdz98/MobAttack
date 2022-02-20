package dev.sasukector.mobattack.commands;

import dev.sasukector.mobattack.controllers.GameController;
import dev.sasukector.mobattack.helpers.ServerUtilities;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
                        case "kit" -> {
                            player.getInventory().clear();

                            ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
                            helmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
                            helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
                            helmet.addUnsafeEnchantment(Enchantment.MENDING, 1);
                            helmet.addUnsafeEnchantment(Enchantment.OXYGEN, 3);
                            helmet.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
                            player.getInventory().setHelmet(helmet);

                            ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
                            chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
                            chestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
                            chestplate.addUnsafeEnchantment(Enchantment.MENDING, 1);
                            player.getInventory().setChestplate(chestplate);

                            ItemStack leggings = new ItemStack(Material.NETHERITE_LEGGINGS);
                            leggings.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
                            leggings.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
                            leggings.addUnsafeEnchantment(Enchantment.MENDING, 1);
                            player.getInventory().setLeggings(leggings);

                            ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS);
                            boots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
                            boots.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
                            boots.addUnsafeEnchantment(Enchantment.MENDING, 1);
                            boots.addUnsafeEnchantment(Enchantment.SOUL_SPEED, 3);
                            boots.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 3);
                            boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 4);
                            player.getInventory().setBoots(boots);

                            player.getInventory().setItemInOffHand(new ItemStack(Material.TOTEM_OF_UNDYING));

                            ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
                            sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
                            sword.addUnsafeEnchantment(Enchantment.SWEEPING_EDGE, 3);
                            sword.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
                            sword.addUnsafeEnchantment(Enchantment.MENDING, 1);
                            sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
                            player.getInventory().addItem(sword);

                            ItemStack sword2 = new ItemStack(Material.NETHERITE_SWORD);
                            sword2.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 5);
                            sword2.addUnsafeEnchantment(Enchantment.SWEEPING_EDGE, 3);
                            sword2.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
                            sword2.addUnsafeEnchantment(Enchantment.MENDING, 1);
                            sword2.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
                            player.getInventory().setItem(27, sword2);

                            ItemStack bow = new ItemStack(Material.BOW);
                            bow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 5);
                            bow.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
                            bow.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
                            bow.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
                            player.getInventory().addItem(bow);

                            ItemStack bow2 = new ItemStack(Material.BOW);
                            bow2.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 5);
                            bow2.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
                            bow2.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
                            bow2.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
                            bow2.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
                            player.getInventory().setItem(28, bow2);

                            player.getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING));
                            player.getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING));
                            player.getInventory().addItem(new ItemStack(Material.SHIELD));
                            player.getInventory().addItem(new ItemStack(Material.EXPERIENCE_BOTTLE, 64));
                            player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
                            player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 64));
                            player.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, 64));
                            player.getInventory().addItem(new ItemStack(Material.ARROW, 64));

                            player.getInventory().setItem(11, new ItemStack(Material.TOTEM_OF_UNDYING));
                            player.getInventory().setItem(20, new ItemStack(Material.TOTEM_OF_UNDYING));
                            player.getInventory().setItem(29, new ItemStack(Material.TOTEM_OF_UNDYING));
                            player.getInventory().setItem(12, new ItemStack(Material.TOTEM_OF_UNDYING));
                            player.getInventory().setItem(21, new ItemStack(Material.TOTEM_OF_UNDYING));
                            player.getInventory().setItem(30, new ItemStack(Material.TOTEM_OF_UNDYING));

                            player.getInventory().setItem(13, new ItemStack(Material.SHIELD));
                            player.getInventory().setItem(22, new ItemStack(Material.SHIELD));
                            player.getInventory().setItem(31, new ItemStack(Material.SHIELD));

                            player.updateInventory();
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
            valid.add("kit");
        }
        Collections.sort(valid);
        return valid;
    }

}
