package cz.larkyy.aquaticcrates.editor.menu;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.editor.EditingPlayer;
import cz.larkyy.aquaticcrates.editor.EditorMenu;
import cz.larkyy.aquaticcrates.editor.item.EditorItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class MainMenu extends EditorMenu {
    public MainMenu() {
        super(54, "Select Or Create a Crate");

        AquaticCrates.getCrateHandler().getCrates().forEach((id, c) -> {
            ItemStack is = new ItemStack(Material.CHEST);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName("§b"+id);
            im.setLore(Arrays.asList(" ","§7[Click] §fto edit"));
            is.setItemMeta(im);
            addEditorItem(new EditorItem(is,(ep,e) -> {
                Player p = Bukkit.getPlayer(ep.getUuid());
                AquaticCrates.getEditingHandler().addEditingPlayer(new EditingPlayer(ep.getUuid(),c));
                p.sendMessage("§fYou are editing: "+id);
            }));
        });
    }
}
