package cz.larkyy.aquaticcratestesting.item.impl;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.item.CustomItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AquaticItem extends CustomItem {

    private final String identifier;

    public AquaticItem(String identifier, String name, List<String> description, int amount, int modeldata) {
        super(name, description, amount, modeldata);
        this.identifier = identifier;
    }

    @Override
    public ItemStack getUnmodifiedItem() {
        return AquaticCratesTesting.getItemHandler().getItem(identifier);
    }
}
