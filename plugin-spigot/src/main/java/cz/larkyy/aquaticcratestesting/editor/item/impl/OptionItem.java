package cz.larkyy.aquaticcratestesting.editor.item.impl;

import cz.larkyy.aquaticcratestesting.editor.Editor;
import cz.larkyy.aquaticcratestesting.editor.datatype.DataType;
import cz.larkyy.aquaticcratestesting.editor.datatype.impl.IntDataType;
import cz.larkyy.aquaticcratestesting.editor.item.EditorItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import xyz.larkyy.menulib.MenuItem;

import java.lang.reflect.Field;
import java.util.Arrays;

public class OptionItem implements EditorItem {

    private final MenuItem.Builder itemBuilder;
    private final Object classInstance;
    private final DataType dataType;
    private final Field f;

    public OptionItem(String id, Object classInstance, Field f, ItemStack is, int slot) {
        dataType = new IntDataType();
        itemBuilder = MenuItem.builder(id,is)
                .slots(Arrays.asList(slot));
        this.classInstance = classInstance;
        this.f = f;
    }

    public void set(Object o) {
        f.setAccessible(true);
        try {
            f.set(classInstance,o);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public MenuItem build(Editor editor) {
        itemBuilder.action(e -> {
           if (e.isLeftClick()) {
               dataType.input(editor.getPlayer());
           }
        });

        return itemBuilder.build();
    }
}
