package de.ketchupbombe.manager;

import de.ketchupbombe.utils.Direction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class ChainChecker {

    private Inventory inventory;
    private ItemStack checkItem;
    private int checkSlot;

    public ChainChecker(Inventory inventory, ItemStack checkItem, int checkSlot) {

        this.inventory = inventory;
        this.checkItem = checkItem;
        this.checkSlot = checkSlot;
    }

    public boolean isChain() {
        int row = 1;
        //go trough each direction an check if there is the same item
        for (Direction direction : Direction.values()) {
            if (hasNext(direction)) {
                for (int i = 0; i <= 10; i++) {
                    if (hasNext(direction)) {
                        checkSlot += direction.getSlot();
                        checkItem = inventory.getItem(checkSlot);
                        row++;
                        if (row >= 4) {
                            return true;
                        }
                    } else {
                        row = 0;
                        break;
                    }

                }
            }
        }
        return false;

    }

    private boolean hasNext(Direction direction) {
        return checkSlot + direction.getSlot() < inventory.getSize() && checkItem.isSimilar(inventory.getItem(checkSlot + direction.getSlot()));
    }
}
