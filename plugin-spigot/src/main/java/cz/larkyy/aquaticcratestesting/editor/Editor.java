package cz.larkyy.aquaticcratestesting.editor;

import cz.larkyy.aquaticcratestesting.editor.annotations.EditorCategory;
import cz.larkyy.aquaticcratestesting.editor.annotations.EditorField;
import cz.larkyy.aquaticcratestesting.editor.annotations.EditorInstance;
import cz.larkyy.aquaticcratestesting.editor.item.EditorItem;
import cz.larkyy.aquaticcratestesting.editor.item.impl.CategoryItem;
import cz.larkyy.aquaticcratestesting.editor.item.impl.OptionItem;
import cz.larkyy.aquaticcratestesting.editor.menus.EditorMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Editor {
    private final Editor previousEditor;
    private final Player p;
    private final Class<?> clazz;
    private final Object classInstance;

    private final EditorMenu editorMenu;

    public Editor(Editor previousEditor, Player p, Class<?> editingClass, Object classInstance, String title) {
        this.previousEditor = previousEditor;
        this.p = p;
        this.classInstance = classInstance;
        this.clazz = editingClass;
        this.editorMenu = new EditorMenu(title,this);
    }

    public void open(Player p) {
        for (EditorItem i : getClassItems()) {
            editorMenu.addItem(i.build(this));
        }

        p.openInventory(editorMenu.build().getInventory());
    }

    public Player getPlayer() {
        return p;
    }

    public Editor getPreviousEditor() {
        return previousEditor;
    }

    private List<EditorItem> getClassItems() {
        List<EditorItem> items = new ArrayList<>();
        for (Field f : clazz.getDeclaredFields()) {

            if (f.isAnnotationPresent(EditorField.class)) {
                f.setAccessible(true);

                EditorField a = f.getAnnotation(EditorField.class);
                EditorItem i = getItem(classInstance,a,f);
                Bukkit.broadcastMessage("Found Option Item: "+f.getName());
                items.add(i);
            }
            if (f.isAnnotationPresent(EditorInstance.class)) {
                f.setAccessible(true);
                try {
                    Object o = f.get(classInstance);
                    Class<?> superClass = o.getClass().getSuperclass();
                    if (superClass != null) {
                        EditorItem i = findCategoryField(superClass,o);
                        if (i != null) {
                            items.add(i);
                        }
                    }
                    EditorItem i = findCategoryField(o.getClass(),o);
                    if (i != null) {
                        items.add(i);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            if (f.isAnnotationPresent(EditorCategory.class)) {
                f.setAccessible(true);

                try {
                    Object o = f.get(classInstance);
                    if (o instanceof Collection<?>) {
                        ParameterizedType genericType = (ParameterizedType) f.getGenericType();
                        Class<?> genericClass = (Class<?>) genericType.getActualTypeArguments()[0];
                        if (genericClass != null) {
                            Bukkit.broadcastMessage(genericClass.getName());
                        }

                        Collection<?> collection = (Collection<?>) o;

                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                try {
                    Class<?> fieldClazz = f.get(classInstance).getClass();

                    EditorCategory a = f.getAnnotation(EditorCategory.class);
                    EditorItem i = getCategoryItem(f.get(classInstance),a,fieldClazz);
                    items.add(i);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return items;
    }

    private EditorItem findCategoryField(Class<?> clazz, Object classInstance) {
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(EditorCategory.class)) {
                f.setAccessible(true);

                EditorCategory a = f.getAnnotation(EditorCategory.class);
                try {
                    return getCategoryItem(classInstance,a,f.get(classInstance).getClass());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    private EditorItem getCategoryItem(Object instance, EditorCategory a, Class<?> clazz) {
        ItemStack is = new ItemStack(a.material());
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(a.displayName());
        is.setItemMeta(im);

        return new CategoryItem(
                a.id(),
                instance,
                clazz,
                is,
                a.slot(),
                a.title()
        );
    }

    private EditorItem getItem(Object instance, EditorField a, Field f) {
        ItemStack is = new ItemStack(a.material());
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(a.displayName());
        is.setItemMeta(im);

        return new OptionItem(
                a.id(),
                instance,
                f,
                is,
                a.slot()
        );
    }
}
