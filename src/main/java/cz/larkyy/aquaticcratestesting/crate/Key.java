package cz.larkyy.aquaticcratestesting.crate;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.api.AquaticCratesAPI;
import cz.larkyy.aquaticcratestesting.item.CustomItem;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Key {

    private static final NamespacedKey KEY
            = new NamespacedKey(AquaticCratesTesting.getPlugin(AquaticCratesTesting.class),"KeyIdentifier");

    private final CustomItem item;
    private final Crate crate;

    public Key(CustomItem item, Crate crate) {
        this.item = item;
        this.crate = crate;
    }

    public Crate getCrate() {
        return crate;
    }

    public String getIdentifier() {
        return crate.getIdentifier();
    }

    public void give(List<Player> players, int amount) {
        ItemStack is = item.getItem();
        is.setAmount(amount);

        ItemMeta im = is.getItemMeta();
        im.getPersistentDataContainer().set(KEY, PersistentDataType.STRING,crate.getIdentifier());
        is.setItemMeta(im);

        players.forEach(p->p.getInventory().addItem(is));
    }

    public boolean isItemKey(ItemStack is) {
        if (is == null) return false;
        ItemMeta im = is.getItemMeta();
        if (im == null) return false;

        String id = im.getPersistentDataContainer().get(KEY,PersistentDataType.STRING);
        if (id == null) return false;
        else return id.equals(crate.getIdentifier());
    }

    public static Key get(String identifier) {
        return AquaticCratesAPI.getKey(identifier);
    }
}
