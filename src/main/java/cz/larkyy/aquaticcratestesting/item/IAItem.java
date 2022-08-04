package cz.larkyy.aquaticcratestesting.item;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class IAItem extends CustomItem {

    private final String identifier;

    public IAItem(String name, List<String> description, String identifier) {
        super(name, description);
        this.identifier = identifier;
    }

    @Override
    public ItemStack getUnmodifiedItem() {
        return CustomStack.getInstance(identifier).getItemStack();
    }
}
