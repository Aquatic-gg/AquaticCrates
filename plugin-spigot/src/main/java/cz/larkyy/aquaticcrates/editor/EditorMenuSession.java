package cz.larkyy.aquaticcrates.editor;

import cz.larkyy.aquaticcrates.editor.item.EditorItem;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EditorMenuSession implements InventoryHolder {

    private final EditorMenu editorMenu;
    private final EditingPlayer editingPlayer;
    private final Inventory inventory;
    private final Map<Integer,EditorItem> placedItems = new HashMap<>();

    public EditorMenuSession(EditorMenu editorMenu, EditingPlayer editingPlayer) {
        this.editorMenu = editorMenu;
        this.editingPlayer = editingPlayer;

        inventory = Bukkit.createInventory(this,editorMenu.getSize(),editorMenu.getTitle());
        setupItems();
    }

    private void setupItems() {
        for (int i = 0; i < editorMenu.getEditorItems().size(); i++) {
            EditorItem item = editorMenu.getEditorItems().get(i);
            addPlacedItem(item,i);
        }
    }

    private void addPlacedItem(EditorItem item, int slot) {
        inventory.setItem(slot,item.getIs(editingPlayer).clone());
        placedItems.put(slot,item);
    }
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void onClick(InventoryClickEvent e) {
        if (!e.getWhoClicked().getUniqueId().equals(editingPlayer.getUuid())) return;

        e.setCancelled(true);
        var item = placedItems.get(e.getSlot());
        if (item != null) {
            item.click(editingPlayer,e);
        }
    }
}
