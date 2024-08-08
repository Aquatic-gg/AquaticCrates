package cz.larkyy.aquaticcrates.menu;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MenuListener implements Listener {

    private static MenuListener instance = null;

    public MenuListener(JavaPlugin plugin) {
        if (instance == null) {
            instance = this;
            Bukkit.getPluginManager().registerEvents(this,plugin);
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof Menu m) {
            e.setCancelled(true);
            m.activate(e);
        }
    }
}
