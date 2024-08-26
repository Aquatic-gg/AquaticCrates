package cz.larkyy.aquaticcrates.item;

import cz.larkyy.aquaticcrates.AquaticCrates;
import gg.aquatic.aquaticseries.lib.item.CustomItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class AquaticItem extends CustomItem {

    private final String identifier;

    private final String name;
    private final List<String> description;
    private final int amount;
    private final int modeldata;
    private final Map<Enchantment,Integer> enchantments;
    private final List<ItemFlag> flags;
    private final EntityType spawnerEntityType;

    public AquaticItem(String identifier, String name, List<String> description, int amount, int modeldata, Map<Enchantment,Integer> enchantments, List<ItemFlag> flags, EntityType spawnerEntityType) {
        super();
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.modeldata = modeldata;
        this.enchantments = enchantments;
        this.flags = flags;
        this.spawnerEntityType = spawnerEntityType;
    }

    @Override
    public ItemStack getUnmodifiedItem() {
        return AquaticCrates.getItemHandler().getItem(identifier);
    }

    @Nullable
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public List<String> getDescription() {
        return description;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public int getModelData() {
        return modeldata;
    }

    @Nullable
    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    @Nullable
    @Override
    public List<ItemFlag> getFlags() {
        return flags;
    }

    @Nullable
    @Override
    public EntityType getSpawnerEntityType() {
        return spawnerEntityType;
    }
}
