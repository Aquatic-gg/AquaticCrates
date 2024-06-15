package cz.larkyy.aquaticcrates.crate.inventories;

import cz.larkyy.aquaticcrates.crate.MultiCrate;
import cz.larkyy.aquaticcrates.crate.PlacedCrate;
import cz.larkyy.aquaticcrates.crate.PlacedMultiCrate;
import cz.larkyy.aquaticcrates.player.CratePlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.larkyy.menulib.Menu;
import xyz.larkyy.menulib.MenuItem;

import java.util.Map;

public class MultiPreviewGUI {
    private final MultiCrate multiCrate;
    private final Menu.Builder mb;

    public MultiPreviewGUI(MultiCrate crate, Menu.Builder mb) {
        this.multiCrate = crate;
        this.mb = mb;
    }

    public void open(Player p, PlacedMultiCrate pmc) {
        Menu.Builder builder = mb.clone();
        Menu m = builder.build();

        for (Map.Entry<String, MenuItem> entry : m.getItems().entrySet()) {
            String id = entry.getKey();
            MenuItem mi = entry.getValue();

            if (id.startsWith("crate-")) {
                String crateId = id.substring(6);
                if (!pmc.getPlacedCrates().containsKey(crateId)) continue;
                PlacedCrate pc = pmc.getPlacedCrates().get(crateId);
                mi.addAction(e -> {
                    if (e.isRightClick()) {
                        pc.getCrate().openPreview(p,pc);
                    } else {
                        p.closeInventory();
                        pc.getCrate().open(CratePlayer.get(p),pc,false);
                    }
                });
            }
        }

        MenuItem mi;
        mi = m.getItem("close-button");
        if (mi != null) {
            mi.addAction(e -> {
                p.closeInventory();
            });
        }
        for (ItemStack is : m.getInventory().getContents()) {
            if (is == null) {
                continue;
            }
            if (is.getItemMeta() == null) {
                continue;
            }

            if (is.getItemMeta().getLore() == null) {
                continue;
            }

            ItemMeta im = is.getItemMeta();
            im.setLore(PlaceholderAPI.setPlaceholders(p.getPlayer(),im.getLore()));
            is.setItemMeta(im);
        }

        p.openInventory(m.getInventory());
    }
}
