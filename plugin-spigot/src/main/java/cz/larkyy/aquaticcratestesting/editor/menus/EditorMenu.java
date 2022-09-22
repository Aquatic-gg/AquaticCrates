package cz.larkyy.aquaticcratestesting.editor.menus;

import cz.larkyy.aquaticcratestesting.editor.EditorItem;
import org.bukkit.entity.Player;
import xyz.larkyy.menulib.Menu;

public interface EditorMenu {

    void open(Player p);

    Menu.Builder getMenuBuilder();

    void addEditorItem(EditorItem editorItem);

}
