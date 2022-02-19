package dev.sasukector.mobattack.events;

import dev.sasukector.mobattack.helpers.ServerUtilities;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class GameEvents implements Listener {

    @EventHandler
    public void onPlayerDismount(EntityDismountEvent event) {
        if (event.getEntity() instanceof Player player && event.getDismounted() instanceof ArmorStand armorStand) {
            if (armorStand.getScoreboardTags().contains("chair")) {
                armorStand.remove();
                player.playSound(player.getLocation(), Sound.ENTITY_PIG_SADDLE, 1, 1.6f);
                player.sendActionBar(ServerUtilities.getMiniMessage().parse(
                        "<color:#FED9B7>Te has levantado</color>"
                ));
                player.teleport(player.getLocation().add(0, 0.5, 0));
            }
        }
    }

}
