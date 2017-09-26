package de.ketchupbombe;

import de.ketchupbombe.listeners.InventoryClickListener;
import de.ketchupbombe.listeners.InventoryCloseListener;
import de.ketchupbombe.listeners.PlayerInteractAtEntityListener;
import de.ketchupbombe.utils.Variables;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class VierGewinnt extends JavaPlugin {

    private static VierGewinnt INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        init();
        Bukkit.getConsoleSender().sendMessage(Variables.getPrefix() + "§aPlugin aktiviert!");

    }

    @Override
    public void onDisable() {

        Bukkit.getConsoleSender().sendMessage(Variables.getPrefix() + "§aPlugin aktiviert!");
    }

    /*
       register Events
     */
    private void init() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new InventoryClickListener(), this);
        pm.registerEvents(new InventoryCloseListener(), this);
        pm.registerEvents(new PlayerInteractAtEntityListener(), this);
    }

    public static VierGewinnt getInstance() {
        return INSTANCE;
    }
}
