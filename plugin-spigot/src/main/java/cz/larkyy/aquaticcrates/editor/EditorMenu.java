package cz.larkyy.aquaticcrates.editor;

import cz.larkyy.aquaticcrates.editor.item.EditorItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class EditorMenu {

    private final int size;
    private final String title;
    private final List<EditorItem> editorItems = new ArrayList<>();

    public EditorMenu(int size, String title) {
        this.size = size;
        this.title = title;
    }

    public void addEditorItem(EditorItem editorItem) {
        this.editorItems.add(editorItem);
    }

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public List<EditorItem> getEditorItems() {
        return editorItems;
    }

    public void open(EditingPlayer editingPlayer) {
        Player p = Bukkit.getPlayer(editingPlayer.getUuid());
        p.openInventory(new EditorMenuSession(this,editingPlayer).getInventory());
    }
}
