package cz.larkyy.aquaticcrates.editor.item;

import cz.larkyy.aquaticcrates.editor.EditingPlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class EditorItem {

    private final ItemStack is;
    private final BiConsumer<EditingPlayer, InventoryClickEvent> onClick;
    private BiConsumer<EditingPlayer,ItemStack> getItemStackConsumer = (ep,is) -> {};

    public EditorItem(ItemStack is, BiConsumer<EditingPlayer, InventoryClickEvent> onClick) {
        this.is = is;
        this.onClick = onClick;
    }

    public void click(EditingPlayer editingPlayer, InventoryClickEvent event) {
        onClick.accept(editingPlayer, event);
    }

    public ItemStack getIs(EditingPlayer editingPlayer) {
        var newIs = is.clone();
        getItemStackConsumer.accept(editingPlayer,newIs);
        return newIs;
    }

    public void setGetItemStackConsumer(BiConsumer<EditingPlayer, ItemStack> getItemStackConsumer) {
        this.getItemStackConsumer = getItemStackConsumer;
    }
}
