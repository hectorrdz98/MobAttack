package dev.sasukector.hundreddaysants.commands;

import dev.sasukector.hundreddaysants.HundredDaysAnts;
import dev.sasukector.hundreddaysants.helpers.ServerUtilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.getVehicle() == null) {
                if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                    ArmorStand armorStand = (ArmorStand)
                            player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
                    armorStand.setVisible(false);
                    armorStand.setInvulnerable(true);
                    armorStand.setCanMove(false);
                    armorStand.setMarker(true);
                    armorStand.setAI(false);
                    armorStand.getScoreboardTags().add("chair");
                    armorStand.addPassenger(player);
                    player.playSound(player.getLocation(), Sound.ENTITY_PIG_SADDLE, 1, 1.6f);
                    Bukkit.getScheduler().runTaskLater(HundredDaysAnts.getInstance(), () ->
                            player.sendActionBar(ServerUtilities.getMiniMessage().parse(
                                    "<color:#FED9B7>Sentado, </color><bold><color:#F07167>[Shift]</color></bold><color:#FED9B7> para desmontarte</color>"
                            )), 5);
                } else {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1.6f);
                    player.sendActionBar(ServerUtilities.getMiniMessage().parse(
                            "<color:#F07167>No te puedes sentar en el aire</color>"
                    ));
                }
            } else {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1.6f);
                player.sendActionBar(ServerUtilities.getMiniMessage().parse(
                        "<color:#F07167>Ya estás sentado en algo</color>"
                ));
            }
        }
        return true;
    }

}
