package de.ketchupbombe.listeners;

import de.ketchupbombe.utils.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ConcurrentHashMap<Player, Game> games = Game.getGames();
        if (games.containsKey(p)) {
            games.get(p).click(e);
        }
    }
}
