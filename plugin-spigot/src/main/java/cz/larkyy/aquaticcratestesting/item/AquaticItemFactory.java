package cz.larkyy.aquaticcratestesting.item;

import xyz.larkyy.itemlibrary.CustomItem;
import xyz.larkyy.itemlibrary.ItemFactory;

import java.util.List;

public class AquaticItemFactory implements ItemFactory {
    @Override
    public CustomItem create(String s, String s1, List<String> list, int i, int i1) {
        return new AquaticItem(s,s1,list,i,i1);
    }
}
