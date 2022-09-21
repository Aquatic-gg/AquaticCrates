package cz.larkyy.aquaticcratestesting.editor;

import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.editor.annotations.EditorField;
import cz.larkyy.aquaticcratestesting.editor.annotations.EditorInstance;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EditorUtils {

    public static List<EditorItem> getItems(Crate crate) {
        return getClassItems(crate.getClass(), crate);
    }

    private static List<EditorItem> getClassItems(Class<?> clazz, Object instance) {
        List<EditorItem> items = new ArrayList<>();
        for (Field f : clazz.getDeclaredFields()) {

            /*
            ParameterizedType genericType = (ParameterizedType) f.getGenericType();
            Class<?> genericClass = (Class<?>) genericType.getActualTypeArguments()[0];
            if (genericClass != null) {
                Bukkit.broadcastMessage(genericClass.getName());
            }

             */

            if (f.isAnnotationPresent(EditorField.class)) {
                f.setAccessible(true);
                try {
                    if (f.get(instance) instanceof Collection<?>) {
                        //Type type = f.getGenericType();
                        ParameterizedType genericType = (ParameterizedType) f.getGenericType();
                        Class<?> genericClass = (Class<?>) genericType.getActualTypeArguments()[0];
                        if (genericClass != null) {
                            Bukkit.broadcastMessage(genericClass.getName());
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                EditorField a = f.getAnnotation(EditorField.class);
                EditorItem i = getItem(instance,a,f);
                items.add(i);
            }
            if (f.isAnnotationPresent(EditorInstance.class)) {
                f.setAccessible(true);
                try {
                    Object o = f.get(instance);
                    Class<?> superClass = o.getClass().getSuperclass();
                    if (superClass != null) {
                        items.addAll(getClassItems(superClass,o));
                    }
                    items.addAll(getClassItems(o.getClass(),o));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return items;
    }

    private static EditorItem getItem(Object instance, EditorField a, Field f) {
        ItemStack is = new ItemStack(a.material());
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(a.displayName());
        is.setItemMeta(im);

        return new EditorItem(
                a.id(),
                instance,
                f,
                is,
                a.page(),
                a.type(),
                a.slot()
                );
    }
}
