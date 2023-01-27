package cz.larkyy.aquaticcratestesting.item;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import xyz.larkyy.itemlibrary.CustomItem;
import xyz.larkyy.itemlibrary.ItemFactory;
import xyz.larkyy.itemlibrary.UnknownCustomItemException;

import java.util.List;
import java.util.Map;

public class AquaticItemFactory implements ItemFactory {
    @Override
    public CustomItem create(String s, String s1, List<String> list, int i, int i1, Map<Enchantment,Integer> enchantments, List<ItemFlag> flags) throws UnknownCustomItemException {
        return new AquaticItem(s,s1,list,i,i1,enchantments,flags);
    }
}
