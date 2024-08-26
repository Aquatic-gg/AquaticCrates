package cz.larkyy.aquaticcrates.item;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.config.Config;
import gg.aquatic.aquaticseries.lib.item.CustomItem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class ItemHandler {

    private final Config databaseCfg = new Config(AquaticCrates.instance(),"itemdatabase.yml");
    private final Map<String, ItemStack> database;

    public ItemHandler() {
        database = new HashMap<>();
    }

    public void load() {
        CustomItem.Companion.getCustomItemHandler().registerItemFactory("aquatic",new AquaticItemFactory());
        database.clear();
        databaseCfg.load();
        loadItems();
    }

    private void loadItems() {
        if (!cfg().contains("items")) {
            return;
        }

        for (String key : cfg().getConfigurationSection("items").getKeys(false)) {
            ItemStack is = cfg().getItemStack("items."+key);
            database.put(key,is);
        }
    }

    private FileConfiguration cfg() {
        return databaseCfg.getConfiguration();
    }

    public ItemStack getItem(String id) {
        ItemStack is = database.get(id);
        if (is == null) {
            return is;


        }
        return is.clone();
    }

    public void addItem(String id, ItemStack is) {
        database.put(id,is);
        saveItem(id,is);
    }

    private void saveItem(String id, ItemStack is) {
        new BukkitRunnable() {
            @Override
            public void run() {
                cfg().set("items."+id,is);
                databaseCfg.save();
            }
        }.runTaskAsynchronously(AquaticCrates.instance());
    }

    public Map<String, ItemStack> getItems() {
        return database;
    }
}
