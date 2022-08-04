package cz.larkyy.aquaticcratestesting.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class VanillaItem extends CustomItem {

    private final Material material;

    public VanillaItem(Material material, String name, List<String> description) {
        super(name, description);
        this.material = material;
    }

    @Override
    public ItemStack getUnmodifiedItem() {
        return new ItemStack(material);
    }
}
