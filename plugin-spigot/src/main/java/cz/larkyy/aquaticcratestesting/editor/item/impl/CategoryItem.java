package cz.larkyy.aquaticcratestesting.editor.item.impl;

import cz.larkyy.aquaticcratestesting.editor.NewEditor;
import cz.larkyy.aquaticcratestesting.editor.item.EditorItem;
import cz.larkyy.aquaticcratestesting.editor.menus.NewEditorMenu;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import xyz.larkyy.menulib.MenuItem;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CategoryItem implements EditorItem {

    private final MenuItem.Builder itemBuilder;
    private final Object classInstance;
    private final Class<?> clazz;
    private final String title;

    public CategoryItem(String id, Object classInstance, Class<?> clazz, ItemStack is, int slot, String title) {
        itemBuilder = MenuItem.builder(id,is)
                .slots(Arrays.asList(slot));
        this.classInstance = classInstance;
        this.clazz = clazz;
        this.title = title;
    }

    public MenuItem build(NewEditor editor) {
        itemBuilder.action(e -> {
            if (e.isLeftClick()) {
                Bukkit.broadcastMessage("Clicked on a category of: "+clazz.getName());
                new NewEditor(editor, editor.getPlayer(), clazz,classInstance,title).open(editor.getPlayer());
            }
        });
        return itemBuilder.build();
    }

}
