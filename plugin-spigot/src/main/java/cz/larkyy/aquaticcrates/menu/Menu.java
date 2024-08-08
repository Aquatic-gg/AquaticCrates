package cz.larkyy.aquaticcrates.menu;

import cz.larkyy.aquaticcrates.AquaticCrates;
import gg.aquatic.aquaticseries.lib.StringExtKt;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Menu implements InventoryHolder {

    private final JavaPlugin plugin;
    private final NamespacedKey key;
    private final Map<String, MenuItem> menuItems;
    private final String title;

    private final Inventory inventory;

    public Menu(JavaPlugin plugin, NamespacedKey key, Map<String, MenuItem> menuItems, String title, int size) {
        this.plugin = plugin;
        this.key = key;
        this.menuItems = menuItems;
        this.title = title;

        inventory = AquaticCrates.aquaticSeriesLib.getAdapter().getInventoryAdapter().create(StringExtKt.toAquatic(title),this,size);
        loadItems();
    }

    public void loadItems() {
        for (MenuItem i : menuItems.values()) {
            i.getSlots().forEach(slot -> {
                inventory.setItem(slot,i.getItemStack());
            });
        }
    }

    public MenuItem getItem(String s) {
        return menuItems.get(s);
    }

    public Map<String, MenuItem> getItems() {
        return menuItems;
    }

    public void addItem(MenuItem item) {
        menuItems.put(item.getId(),item);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void activate(InventoryClickEvent e) {
        ItemStack is = e.getCurrentItem();
        if (is == null) return;
        ItemMeta im = is.getItemMeta();
        if (im == null) return;
        if (!im.getPersistentDataContainer().has(key,PersistentDataType.STRING)) return;
        String s = im.getPersistentDataContainer().get(key,PersistentDataType.STRING);
        MenuItem mi = menuItems.get(s);
        if (mi == null) return;
        mi.activate(e);
    }

    @Override
    public Menu clone() {
        return new Menu(plugin,key,menuItems,title,inventory.getSize());
    }

    public static Builder builder(JavaPlugin plugin) {
        return new Builder(plugin);
    }


    public static class Builder {
        private final JavaPlugin plugin;
        private final NamespacedKey key;
        private Map<String, MenuItem> menuItems;
        private String title;
        private int size;

        public Builder(JavaPlugin plugin) {
            this.plugin = plugin;
            this.key = new NamespacedKey(plugin,"MenuLib_Key");
            menuItems = new HashMap<>();
            size = 9;
        }

        public Builder addItem(MenuItem item) {
            ItemStack is = item.getItemStack();
            ItemMeta im = is.getItemMeta();
            im.getPersistentDataContainer().set(key, PersistentDataType.STRING, item.getId());
            is.setItemMeta(im);
            menuItems.put(item.getId(),item);
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder size(int i) {
            this.size = i;
            return this;
        }

        public Menu build() {
            new MenuListener(plugin);
            return new Menu(plugin,key,menuItems,title,size);
        }

        public Builder clone() {
            Builder b = new Builder(plugin);
            b.menuItems = new HashMap<>();
            menuItems.forEach((s,mi) -> {
                b.menuItems.put(s,mi.clone());
            });
            b.title = title;
            b.size = size;

            return b;
        }
    }

}
