package cz.larkyy.aquaticcratestesting.item;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.config.Config;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.itemlibrary.CustomItem;

import java.util.HashMap;
import java.util.Map;

public class ItemHandler {

    private final Config databaseCfg = new Config(AquaticCratesTesting.instance(),"itemdatabase.yml");
    private final Map<String, ItemStack> database;

    public ItemHandler() {
        database = new HashMap<>();
    }

    public void load() {
        CustomItem.getCustomItemHandler().addItemFactory("aquatic",new AquaticItemFactory());
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
        }.runTaskAsynchronously(AquaticCratesTesting.instance());
    }

    public Map<String, ItemStack> getItems() {
        return database;
    }
}
