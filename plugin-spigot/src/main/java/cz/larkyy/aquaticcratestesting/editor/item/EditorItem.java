package cz.larkyy.aquaticcratestesting.editor.item;


import cz.larkyy.aquaticcratestesting.editor.NewEditor;
import cz.larkyy.aquaticcratestesting.editor.menus.NewEditorMenu;
import xyz.larkyy.menulib.MenuItem;

public interface EditorItem {

    public MenuItem build(NewEditor editor);

}
