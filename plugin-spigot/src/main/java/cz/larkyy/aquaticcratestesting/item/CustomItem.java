package cz.larkyy.aquaticcratestesting.item;

import cz.larkyy.aquaticcratestesting.item.impl.*;
import cz.larkyy.aquaticcratestesting.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.larkyy.colorutils.Colors;

import java.util.List;

public abstract class CustomItem {

    private final String name;
    private final List<String> description;
    private final int amount;
    private final int modeldata;

    public CustomItem(String name, List<String> description, int amount, int modeldata) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.modeldata = modeldata;
    }

    public void giveItem(Player player) {
        giveItem(player,amount);
    }

    public void giveItem(Player player, int amount) {
        ItemStack is = getItem();
        is.setAmount(amount);

        player.getInventory().addItem(is);
    }

    public ItemStack getItem() {
        ItemStack is = getUnmodifiedItem();
        ItemMeta im = is.getItemMeta();

        if (im == null) {
            return is;
        }

        if (name != null) {
            im.setDisplayName(Colors.format(name));
        }

        if (description != null) {
            im.setLore(Colors.format(description));
        }

        if (modeldata > 0) {
            im.setCustomModelData(modeldata);
        }

        is.setItemMeta(im);
        is.setAmount(amount);
        return is;
    }

    public abstract ItemStack getUnmodifiedItem();

    public static CustomItem create(String namespace, String name, List<String> description,int amount, int modeldata) {
        String[] strs = namespace.split(":");
        if (strs.length == 1) {
            return new VanillaItem(Material.valueOf(strs[0].toUpperCase()),name,description,amount,modeldata);
        }
        String provider = strs[0].toLowerCase();
        String identifier = namespace.substring(provider.length()+1);

        switch (provider) {
            case "itemsadder": {
                return new IAItem(identifier,name,description,amount,modeldata);
            }
            case "oraxen": {
                return new OraxenItem(identifier,name,description,amount,modeldata);
            }
            case "hdb": {
                return new HDBItem(identifier,name,description,amount,modeldata);
            }
            case "mythicitem": {
                return new MMItem(identifier,name,description,amount,modeldata);
            }
            case "aquatic": {
                return new AquaticItem(identifier,name,description,amount,modeldata);
            }
        }
        return null;
    }
}
