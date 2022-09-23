package cz.larkyy.aquaticcratestesting.editor;

import org.bukkit.inventory.ItemStack;
import xyz.larkyy.menulib.MenuItem;

import java.lang.reflect.Field;
import java.util.Arrays;

public class EditorItem extends MenuItem {

    private final Object classInstance;
    private final Field f;
    private final ItemStack is;

    public EditorItem(String id, Object classInstance, Field f, ItemStack is, int slot) {
        super(id,is,e -> {}, Arrays.asList(slot));
        this.classInstance = classInstance;
        this.is = is;
        this.f = f;
    }

    public void click() {

    }

    public void set(Object o) {
        f.setAccessible(true);
        try {
            f.set(classInstance,o);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Object get() {
        try {
            return f.get(classInstance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String field() {
        return f.getName();
    }
}
