package cz.larkyy.aquaticcrates.crate;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.api.AquaticCratesAPI;
import gg.aquatic.aquaticseries.lib.AquaticSeriesLib;
import gg.aquatic.aquaticseries.lib.StringExtKt;
import gg.aquatic.aquaticseries.lib.adapt.AquaticString;
import gg.aquatic.aquaticseries.lib.item.CustomItem;
import gg.aquatic.aquaticseries.lib.util.CollectionExtKt;
import gg.aquatic.aquaticseries.spigot.adapt.SpigotString;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Key {

    private static final NamespacedKey KEY
            = new NamespacedKey(AquaticCrates.getPlugin(AquaticCrates.class), "KeyIdentifier");

    private final CustomItem item;
    private final Crate crate;

    private final boolean requiresCrateToOpen;
    private final boolean mustBeHeld;

    public Key(CustomItem item, Crate crate, boolean requiresCrateToOpen, boolean mustBeHeld) {
        this.item = item;
        this.crate = crate;
        this.requiresCrateToOpen = requiresCrateToOpen;
        this.mustBeHeld = mustBeHeld;
    }

    public Crate getCrate() {
        return crate;
    }

    public String getIdentifier() {
        return crate.getIdentifier();
    }

    public AquaticString getDisplayName() {
        return crate.getDisplayName();
    }

    public void give(List<Player> players, int amount) {
        ItemStack is = getItem(amount);

        players.forEach(p -> {
            var im = is.getItemMeta();

            var displayName = StringExtKt.updatePAPIPlaceholders(Objects.requireNonNullElse(AquaticSeriesLib.Companion.getINSTANCE().getAdapter().getItemStackAdapter().getAquaticDisplayName(im), StringExtKt.toAquatic("")), p);
            var previouslore = AquaticSeriesLib.Companion.getINSTANCE().getAdapter().getItemStackAdapter().getAquaticLore(im);
            var newLore = new ArrayList<AquaticString>();
            for (AquaticString aquaticString : previouslore) {
                newLore.add(StringExtKt.updatePAPIPlaceholders(aquaticString,p));
            }

            AquaticSeriesLib.Companion.getINSTANCE().getAdapter().getItemStackAdapter().displayName(displayName,im);
            AquaticSeriesLib.Companion.getINSTANCE().getAdapter().getItemStackAdapter().lore(newLore,im);

            is.setItemMeta(im);
            var map = p.getInventory().addItem(is.clone());
            map.forEach((i, item) -> {
                p.getLocation().getWorld().dropItem(p.getLocation(), item);
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

    public ItemStack getItem(int amount) {
        ItemStack is = item.getItem();
        is.setAmount(amount);

        ItemMeta im = is.getItemMeta();
        im.getPersistentDataContainer().set(KEY, PersistentDataType.STRING, crate.getIdentifier());
        is.setItemMeta(im);

        return is;
    }

    public static Key get(String identifier) {
        return AquaticCratesAPI.getKey(identifier);
    }

    public static Key get(ItemStack is) {
        if (is == null) return null;
        ItemMeta im = is.getItemMeta();
        if (im == null) return null;

        String id = im.getPersistentDataContainer().get(KEY, PersistentDataType.STRING);
        if (id == null) return null;
        else return get(id);
    }

    public boolean requiresCrateToOpen() {
        return requiresCrateToOpen;
    }

    public boolean isMustBeHeld() {
        return mustBeHeld;
    }
}
