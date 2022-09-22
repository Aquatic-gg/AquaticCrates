package cz.larkyy.aquaticcratestesting.editor.annotations;

import org.bukkit.Material;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface EditorField {
    String id();
    Material material() default Material.STONE;
    String displayName() default "";
    int slot();
}
