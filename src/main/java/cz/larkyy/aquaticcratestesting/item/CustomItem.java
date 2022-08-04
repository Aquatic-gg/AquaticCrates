package cz.larkyy.aquaticcratestesting.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class CustomItem {

    private final String name;
    private final List<String> description;

    public CustomItem(String name, List<String> description) {
        this.name = name;
        this.description = description;
    }

    public void giveItem(Player player) {
        giveItem(player,1);
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
            im.setDisplayName(name);
        }

        if (description != null) {
            im.setLore(description);
        }

        is.setItemMeta(im);
        return is;
    }

    public abstract ItemStack getUnmodifiedItem();
}
