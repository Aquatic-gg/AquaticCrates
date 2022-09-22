package cz.larkyy.aquaticcratestesting.editor.item.impl;

import cz.larkyy.aquaticcratestesting.editor.Editor;
import cz.larkyy.aquaticcratestesting.editor.NewEditor;
import cz.larkyy.aquaticcratestesting.editor.item.EditorItem;
import cz.larkyy.aquaticcratestesting.editor.menus.NewEditorMenu;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.larkyy.menulib.MenuItem;

import java.lang.reflect.Field;
import java.util.Arrays;

public class OptionItem implements EditorItem {

    private final MenuItem.Builder itemBuilder;
    private final Object classInstance;
    private final Field f;
    private final Editor.FieldType type;

    public OptionItem(String id, Object classInstance, Field f, ItemStack is, Editor.FieldType type, int slot) {
        itemBuilder = MenuItem.builder(id,is)
                .slots(Arrays.asList(slot));
        this.classInstance = classInstance;
        this.type = type;
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

    public MenuItem build(NewEditor editor) {
        itemBuilder.action(e -> {
           if (e.isLeftClick()) {
               Bukkit.broadcastMessage("Clicked on a setting of: "+f.getName());
           }
        });

        return itemBuilder.build();
    }
}
