package cz.larkyy.aquaticcratestesting.crate;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.api.AquaticCratesAPI;
import cz.larkyy.aquaticcratestesting.item.CustomItem;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;

public class Crate {

    private static final NamespacedKey KEY = new NamespacedKey(AquaticCratesTesting.instance(),"CrateIdentifier");

    private final String identifier;
    private final Key key;
    private final String model;

    public Crate(String identifier,CustomItem key, String model) {
        this.identifier = identifier;
        this.key = new Key(key,this);
        this.model = model;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Key getKey() {
        return key;
    }

    public void giveKey(Player player, int amount, boolean virtual) {
        if (virtual) CratePlayer.get(player).addKeys(identifier,amount);
        else key.give(Collections.singletonList(player), amount);
    }

    public void giveCrate(Player player) {
        ItemStack is = new ItemStack(Material.CHEST);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§fCrate: "+identifier);
        im.getPersistentDataContainer().set(KEY, PersistentDataType.STRING,identifier);
        is.setItemMeta(im);

        player.getInventory().addItem(is);
    }

    public void giveKeyAll(int amount, boolean virtual) {
        if (virtual) {
            AquaticCratesAPI.getPlayerHandler().loadPlayers();
            AquaticCratesAPI.getPlayerHandler().getPlayers().forEach(
                    p -> p.addKeys(identifier,amount));
        }
        else key.give(new ArrayList<>(Bukkit.getOnlinePlayers()), amount);
    }

    public PlacedCrate spawn(Location location) {
        return AquaticCratesAPI.getCrateHandler().spawnCrate(location,this);
    }

    public String getModel() {
        return model;
    }

    public static Crate get(String identifier) {
        return AquaticCratesAPI.getCrate(identifier);
    }

    public static Crate get(ItemStack is) {
        if (is == null) return null;
        ItemMeta im = is.getItemMeta();
        if (im == null) return null;

        String id = im.getPersistentDataContainer().get(KEY,PersistentDataType.STRING);
        if (id == null) return null;
        else return get(id);
    }
}
