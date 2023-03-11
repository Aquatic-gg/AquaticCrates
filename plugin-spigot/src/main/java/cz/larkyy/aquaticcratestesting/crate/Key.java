package cz.larkyy.aquaticcratestesting.crate;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.api.AquaticCratesAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.larkyy.itemlibrary.CustomItem;

import java.util.List;

public class Key {

    private static final NamespacedKey KEY
            = new NamespacedKey(AquaticCratesTesting.getPlugin(AquaticCratesTesting.class),"KeyIdentifier");

    private final CustomItem item;
    private final Crate crate;

    private final boolean requiresCrateToOpen;

    public Key(CustomItem item, Crate crate, boolean requiresCrateToOpen) {
        this.item = item;
        this.crate = crate;
        this.requiresCrateToOpen = requiresCrateToOpen;
    }

    public Crate getCrate() {
        return crate;
    }

    public String getIdentifier() {
        return crate.getIdentifier();
    }

    public String getDisplayName() {
        return crate.getDisplayName();
    }

    public void give(List<Player> players, int amount) {
        ItemStack is = item.getItem();
        is.setAmount(amount);

        ItemMeta im = is.getItemMeta();
        im.getPersistentDataContainer().set(KEY, PersistentDataType.STRING,crate.getIdentifier());
        is.setItemMeta(im);

        players.forEach(p->{
            var map = p.getInventory().addItem(is);
            map.forEach((i,item) -> {
                p.getLocation().getWorld().dropItem(p.getLocation(),item);
            });
        });
    }

    public boolean isItemKey(ItemStack is) {
        Key key = get(is);
        if (key == null) {
            return false;
        }
        return key.getIdentifier().equals(getIdentifier());
    }

    public static Key get(String identifier) {
        return AquaticCratesAPI.getKey(identifier);
    }

    public static Key get(ItemStack is) {
        if (is == null) return null;
        ItemMeta im = is.getItemMeta();
        if (im == null) return null;

        String id = im.getPersistentDataContainer().get(KEY,PersistentDataType.STRING);
        if (id == null) return null;
        else return get(id);
    }

    public boolean requiresCrateToOpen() {
        return requiresCrateToOpen;
    }
}
