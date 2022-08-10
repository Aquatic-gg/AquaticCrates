package cz.larkyy.aquaticcratestesting.item.impl;

import cz.larkyy.aquaticcratestesting.item.CustomItem;
import io.th0rgal.oraxen.items.OraxenItems;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OraxenItem extends CustomItem {

    private final String identifier;

    public OraxenItem(String name, List<String> description, String identifier) {
        super(name, description);
        this.identifier = identifier;
    }

    @Override
    public ItemStack getUnmodifiedItem() {
        return OraxenItems.getItemById(identifier).build();
    }
}
