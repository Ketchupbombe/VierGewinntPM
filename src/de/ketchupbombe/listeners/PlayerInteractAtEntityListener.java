package de.ketchupbombe.listeners;

import de.ketchupbombe.utils.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class PlayerInteractAtEntityListener implements Listener {

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        if (p.isSneaking()) {
            if (e.getRightClicked() instanceof Player) {
                Player target = (Player) e.getRightClicked();
                // starting new game
                new Game(p, target).start();
            }
        }
    }

}
