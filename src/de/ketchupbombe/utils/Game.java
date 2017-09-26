package de.ketchupbombe.utils;

import de.ketchupbombe.VierGewinnt;
import de.ketchupbombe.manager.ChainChecker;
import de.ketchupbombe.manager.PlayerPair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class Game {

    private PlayerPair playerPair;
    private boolean itemDropping = false;
    private boolean running = false;
    private boolean updatingGamesInv;
    private static ConcurrentHashMap<Player, Game> GAMES = new ConcurrentHashMap<>();

    /*
    Constructor
     */
    public Game(Player player1, Player player2) {
        playerPair = new PlayerPair(player1, player2);

        // add players to active games ConcurrentHashMap
        GAMES.put(player1, this);
        GAMES.put(player2, this);
    }

    public void start() {
        if (playerPair == null) {
            return;
        }
        // create inventory
        Inventory inv = buildInv("§e" + playerPair.getCurrentPlayer().getName() + "'s zug", null);
        playerPair.getPlayer1().openInventory(inv);
        playerPair.getPlayer2().openInventory(inv);
        running = true;
    }

    public void end() {
        running = false;

        // remove players from active games ConcurrentHashMap
        GAMES.remove(playerPair.getPlayer1());
        GAMES.remove(playerPair.getPlayer2());
        playerPair.getPlayer1().closeInventory();
        playerPair.getPlayer2().closeInventory();
    }

    private ItemStack placeHolder() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE).withDamage((short) 7).withName(" ").build();
    }

    private ItemStack greenButton() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE).withDamage((short) 5).withName("§a▼").build();
    }

    private ItemStack player1Item() {
        return new ItemBuilder(Material.REDSTONE_BLOCK).withName("§cSpieler 1").build();
    }

    private ItemStack player2Item() {
        return new ItemBuilder(Material.GOLD_BLOCK).withName("§6Spieler 2").build();
    }

    private void updateGamesInv() {
        if (running) {
            updatingGamesInv = true;
            ItemStack[] contents = playerPair.getPlayer1().getOpenInventory().getTopInventory().getContents();
            Inventory inv = buildInv("§e" + playerPair.getCurrentPlayer().getName() + "'s zug", contents);
            playerPair.getPlayer1().openInventory(inv);
            playerPair.getPlayer2().openInventory(inv);
            updatingGamesInv = false;
        }
    }

    /*
    Calling this method in InventoryCkickListener
     */
    public void click(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();
        int slot = e.getSlot();
        e.setCancelled(true);
        if (!running) {
            return;
        }
        if (!playerPair.isPlayerTurn(p)) {
            return;
        }
        if (itemDropping) {
            return;
        }
        try {
            if (inv.getItem(slot).getItemMeta().getDisplayName().equalsIgnoreCase("§a▼")) {

                if (p.equals(playerPair.getPlayer1())) {
                    if (isRowFull(inv, slot)) {
                        playerPair.getPlayer1().sendMessage("Diese Reihe ist bereits voll!");
                        return;
                    }
                    itemDropping = true;
                    FallingAnimation fallingAnimation = new FallingAnimation(player1Item(), inv, slot);
                    fallingAnimation.start();
                    int finalSlot = fallingAnimation.getFinalSlot();
                    itemDropping = false;
                    if (new ChainChecker(inv, player1Item(), finalSlot).isChain()) {
                        end();
                        playerPair.getPlayer1().sendMessage("Du hast das Spiel gewonnen!");
                        playerPair.getPlayer1().playSound(playerPair.getPlayer1().getLocation(), Sound.LEVEL_UP, 2f, 1f);
                        playerPair.getPlayer2().sendMessage("Du hast das Spiel verloren!");
                        playerPair.getPlayer2().playSound(playerPair.getPlayer1().getLocation(), Sound.ANVIL_LAND, 2f, 1f);
                    }
                }
                if (p.equals(playerPair.getPlayer2())) {
                    if (isRowFull(inv, slot)) {
                        playerPair.getPlayer2().sendMessage("Diese Reihe ist bereits voll!");
                        return;
                    }
                    itemDropping = true;
                    FallingAnimation fallingAnimation = new FallingAnimation(player2Item(), inv, slot);
                    fallingAnimation.start();
                    int finalSlot = fallingAnimation.getFinalSlot();
                    itemDropping = false;
                    if (new ChainChecker(inv, player2Item(), finalSlot).isChain()) {
                        end();
                        playerPair.getPlayer1().sendMessage("Du hast das Spiel verloren!");
                        playerPair.getPlayer1().playSound(playerPair.getPlayer1().getLocation(), Sound.ANVIL_LAND, 2f, 1f);
                        playerPair.getPlayer2().sendMessage("Du hast das Spiel gewonnen!!");
                        playerPair.getPlayer2().playSound(playerPair.getPlayer1().getLocation(), Sound.LEVEL_UP, 2f, 1f);
                    }
                }
            }
        } catch (NullPointerException e1) {
        }
    }

    // for simplicity
    private void updateInvs() {
        playerPair.getPlayer1().updateInventory();
        playerPair.getPlayer2().updateInventory();
    }

    private Inventory buildInv(String title, ItemStack[] contents) {
        Inventory inv = Bukkit.createInventory(null, 9 * 7, title);

        if (contents != null)
            inv.setContents(contents);

        inv.setItem(0, placeHolder());
        inv.setItem(8, placeHolder());
        inv.setItem(9, placeHolder());
        inv.setItem(17, placeHolder());
        inv.setItem(18, placeHolder());
        inv.setItem(26, placeHolder());
        inv.setItem(27, placeHolder());
        inv.setItem(35, placeHolder());
        inv.setItem(36, placeHolder());
        inv.setItem(44, placeHolder());
        inv.setItem(45, placeHolder());
        inv.setItem(53, placeHolder());
        inv.setItem(54, placeHolder());
        inv.setItem(62, placeHolder());

        for (int i = 1; i <= 7; i++) {
            inv.setItem(i, greenButton());
        }


        return inv;
    }

    /*
    Getter
     */
    private boolean isRowFull(Inventory inv, int row) {
        return inv.getItem(row + 9) != null;
    }

    public boolean isUpdatingGamesInv() {
        return updatingGamesInv;
    }

    public PlayerPair getPlayerPair() {
        return playerPair;
    }

    public static ConcurrentHashMap<Player, Game> getGames() {
        return GAMES;
    }

    /*
    Class for animation
     */
    private class FallingAnimation extends BukkitRunnable {

        ItemStack itemStack;
        Inventory inventory;
        int slotToFall;
        int previousSlot = -1;
        int increment = 9;
        int size;
        int finalSlot;

        FallingAnimation(ItemStack itemStack, Inventory inventory, int clickedSlot) {
            this.itemStack = itemStack;
            this.inventory = inventory;
            this.slotToFall = clickedSlot + 9;
            this.size = inventory.getSize();
            this.finalSlot = clickedSlot + 9;
        }

        void start() {
            runTaskTimer(VierGewinnt.getInstance(), 0, 2);

        }

        @Override
        public void run() {
            try {
                if (slotToFall < size && (inventory.getItem(slotToFall) == null
                        || inventory.getItem(slotToFall).getType().equals(Material.AIR))) {
                    inventory.setItem(slotToFall, itemStack);
                    if (previousSlot >= 0) {
                        inventory.setItem(previousSlot, new ItemStack(Material.AIR));
                    }
                    updateInvs();
                    previousSlot = slotToFall;
                    slotToFall += increment;
                } else {
                    cancel();
                    playerPair.nextTurn();
                    updateGamesInv();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }

        //to interact with the item
        int getFinalSlot() {
            while (finalSlot < size && (inventory.getItem(finalSlot) == null
                    || inventory.getItem(finalSlot).getType().equals(Material.AIR))) {
                finalSlot += increment;
            }
            return finalSlot - 9;
        }
    }

}
