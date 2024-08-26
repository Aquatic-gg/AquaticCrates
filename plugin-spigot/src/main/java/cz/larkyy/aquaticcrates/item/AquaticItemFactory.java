package cz.larkyy.aquaticcrates.item;

import gg.aquatic.aquaticseries.lib.item.CustomItem;
import gg.aquatic.aquaticseries.lib.item.factory.ItemFactory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class AquaticItemFactory implements ItemFactory {

    @NotNull
    @Override
    public CustomItem create(@NotNull String s, @Nullable String s1, @Nullable List<String> list, int i, int i1, @Nullable Map<Enchantment, Integer> map, @Nullable List<ItemFlag> list1, @Nullable EntityType entityType) {
        return new AquaticItem(s,s1,list,i,i1,map,list1,entityType);
    }
}
