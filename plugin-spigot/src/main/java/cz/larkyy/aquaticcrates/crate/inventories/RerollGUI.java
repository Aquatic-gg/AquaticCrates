package cz.larkyy.aquaticcrates.crate.inventories;

import cz.larkyy.aquaticcrates.crate.reroll.impl.MenuReroll;
import org.bukkit.entity.Player;
import xyz.larkyy.menulib.Menu;
import xyz.larkyy.menulib.MenuItem;

import java.util.Arrays;

public class RerollGUI {

    private final Menu.Builder mb;
    private final int rewardSlot;

    public RerollGUI(Menu.Builder mb, int rewardSlot) {
        this.mb = mb;
        this.rewardSlot = rewardSlot;
    }

    public void open(MenuReroll menuReroll) {
        Player p = menuReroll.getPlayer();

        Menu.Builder builder = mb.clone();
        builder.addItem(
                MenuItem.builder(
                        "reward",
                        menuReroll.getReward().getItem().getItem()
                )
                        .slots(Arrays.asList(rewardSlot))
                        .build());

        Menu m = builder.build();

        MenuItem mi;
        mi = m.getItem("reroll");
        if (mi != null) {
            mi.addAction(e -> {
                menuReroll.getRerollManager().reroll(menuReroll.getPlayer());
                p.closeInventory();
            });
        }
        mi = m.getItem("claim");
        if (mi != null) {
            mi.addAction(e -> {
                menuReroll.getRerollManager().claim(menuReroll.getPlayer());
                p.closeInventory();
            });
        }
        p.openInventory(m.getInventory());
    }
}
