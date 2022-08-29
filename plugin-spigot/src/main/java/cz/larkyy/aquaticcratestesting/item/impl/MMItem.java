package cz.larkyy.aquaticcratestesting.item.impl;

import cz.larkyy.aquaticcratestesting.item.CustomItem;
import io.lumine.mythic.api.MythicProvider;
import io.lumine.mythic.bukkit.adapters.BukkitItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MMItem extends CustomItem {

    private final String identifier;

    public MMItem(String identifier, String name, List<String> description, int amount, int modeldata) {
        super(name, description, amount, modeldata);
        this.identifier = identifier;
    }

    @Override
    public ItemStack getUnmodifiedItem() {
        return ((BukkitItemStack)MythicProvider.get().getItemManager().getItem(identifier).get().generateItemStack(1)).build();
    }
}
