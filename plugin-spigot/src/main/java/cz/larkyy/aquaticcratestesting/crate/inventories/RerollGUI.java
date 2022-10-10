package cz.larkyy.aquaticcratestesting.crate.inventories;

import cz.larkyy.aquaticcratestesting.crate.reroll.RerollManager;
import cz.larkyy.aquaticcratestesting.crate.reroll.impl.MenuReroll;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import org.bukkit.entity.Player;
import xyz.larkyy.menulib.Menu;
import xyz.larkyy.menulib.MenuItem;

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
        builder.addItem(MenuItem.builder("reward",menuReroll.getReward().getItem().getItem()).build());

        Menu m = builder.build();

        MenuItem mi;
        mi = m.getItem("reroll");
        if (mi != null) {
            mi.addAction(e -> {
                p.closeInventory();
                menuReroll.reroll();
            });
        }
        mi = m.getItem("claim");
        if (mi != null) {
            mi.addAction(e -> {
                p.closeInventory();
                menuReroll.claim();
            });
        }
        p.openInventory(m.getInventory());
    }
}
