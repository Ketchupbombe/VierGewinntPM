package de.ketchupbombe.manager;

import org.bukkit.entity.Player;

public class PlayerPair {

    private Player player1;
    private Player player2;
    private boolean player1Turn = true;

    public PlayerPair(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Player getCurrentPlayer() {
        return player1Turn ? player1 : player2;
    }

    public void nextTurn() {
        player1Turn = !player1Turn;
    }

    public boolean isPlayerTurn(Player player) {
        return getCurrentPlayer().equals(player);
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}
