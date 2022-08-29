package cz.larkyy.aquaticcratestesting.item.impl;

import cz.larkyy.aquaticcratestesting.item.CustomItem;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class IAItem extends CustomItem {

    private final String identifier;

    public IAItem(String identifier, String name, List<String> description, int amount, int modeldata) {
        super(name, description, amount, modeldata);
        this.identifier = identifier;
    }

    @Override
    public ItemStack getUnmodifiedItem() {
        return CustomStack.getInstance(identifier).getItemStack();
    }
}
