package cz.larkyy.aquaticcratestesting.editor;

import cz.larkyy.aquaticcratestesting.editor.inputtype.ChatInputEdit;
import cz.larkyy.aquaticcratestesting.editor.menu.MainMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

public class EditingHandler implements Listener {

    private final Map<UUID, EditingPlayer> editingPlayers = new HashMap<>();
    private final Map<UUID,ChatInputEdit> awaitingChatInput = new HashMap<>();

    public Map<UUID, EditingPlayer> getEditingPlayers() {
        return editingPlayers;
    }

    public EditingPlayer getEditingPlayer(UUID uuid) {
        return editingPlayers.get(uuid);
    }

    public void addEditingPlayer(EditingPlayer editingPlayer) {
        this.editingPlayers.put(editingPlayer.getUuid(),editingPlayer);
    }

    public Map<UUID, ChatInputEdit> getAwaitingChatInput() {
        return awaitingChatInput;
    }

    public void addAwaitChatInput(UUID uuid, ChatInputEdit chatInputEdit) {
        this.awaitingChatInput.put(uuid,chatInputEdit);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (awaitingChatInput.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getWhoClicked().getOpenInventory().getTopInventory().getHolder() instanceof EditorMenuSession editorMenu) {
            editorMenu.onClick(e);
        }
    }

    public void openEditorMenu(Player player) {
        var ep = new EditingPlayer(player.getUniqueId(),null);
        new MainMenu().open(ep);
    }
}
