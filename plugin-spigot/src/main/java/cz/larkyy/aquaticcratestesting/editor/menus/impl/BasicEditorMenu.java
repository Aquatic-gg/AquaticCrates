package cz.larkyy.aquaticcratestesting.editor.menus.impl;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.editor.EditorItem;
import cz.larkyy.aquaticcratestesting.editor.menus.EditorMenu;
import org.bukkit.entity.Player;
import xyz.larkyy.menulib.Menu;

public class BasicEditorMenu implements EditorMenu {

    private final Menu.Builder menuBuilder;

    public BasicEditorMenu(String title) {
        menuBuilder = Menu.builder(AquaticCratesTesting.instance())
                .title(title);
    }

    @Override
    public void open(Player p) {
        p.openInventory(menuBuilder.build().getInventory());
    }

    @Override
    public Menu.Builder getMenuBuilder() {
        return menuBuilder;
    }

    @Override
    public void addEditorItem(EditorItem editorItem) {
        menuBuilder.addItem(editorItem);
    }
}
