package cz.larkyy.aquaticcratestesting.editor.annotations;

import cz.larkyy.aquaticcratestesting.editor.Editor;
import org.bukkit.Material;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface EditorField {
    String id();
    Editor.Page page() default Editor.Page.MAIN;
    Editor.FieldType type() default Editor.FieldType.STRING;
    Material material() default Material.STONE;
    String displayName() default "";
    int slot();
}
