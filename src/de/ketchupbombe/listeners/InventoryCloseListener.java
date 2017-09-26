package de.ketchupbombe.listeners;

import de.ketchupbombe.manager.PlayerPair;
import de.ketchupbombe.utils.Game;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        ConcurrentHashMap<Player, Game> games = Game.getGames();
        if (games.containsKey(p)) {
            Game game = games.get(p);
            // check if the close of inventory is caused by updating
            if (!game.isUpdatingGamesInv()) {
                PlayerPair playerPair = game.getPlayerPair();
                game.end();
                if (playerPair.getPlayer1().equals(p)) {
                    playerPair.getPlayer2().sendMessage("Dein Gegner hat das Inventar geschlossen!");
                    playerPair.getPlayer2().playSound(playerPair.getPlayer2().getLocation(), Sound.ANVIL_LAND, 2f, 1f);
                }
                if (playerPair.getPlayer2().equals(p)) {
                    playerPair.getPlayer1().sendMessage("Dein Gegner hat das Inventar geschlossen!");
                    playerPair.getPlayer1().playSound(playerPair.getPlayer1().getLocation(), Sound.ANVIL_LAND, 2f, 1f);
                }
            }
        }
    }

}
