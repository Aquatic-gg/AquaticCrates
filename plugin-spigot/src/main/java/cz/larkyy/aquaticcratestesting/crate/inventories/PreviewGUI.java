package cz.larkyy.aquaticcratestesting.crate.inventories;

import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import org.bukkit.entity.Player;
import xyz.larkyy.menulib.Menu;
import xyz.larkyy.menulib.MenuItem;

import java.util.Arrays;
import java.util.List;

public class PreviewGUI {

    private final Crate crate;
    private final List<Integer> rewardSlots;
    private final Menu.Builder mb;
    private final boolean openableByKey;

    public PreviewGUI(Crate crate, Menu.Builder mb, List<Integer> rewardSlots, boolean openableByKey) {
        this.crate = crate;
        this.rewardSlots = rewardSlots;
        this.mb = mb;
        this.openableByKey = openableByKey;
    }

    public void open(Player p, int page) {
        Menu.Builder builder = mb.clone();
        loadRewardItems(p,builder,page);
        Menu m = builder.build();

        MenuItem mi;
        mi = m.getItem("next-page");
        if (mi != null) {
            mi.addAction(e -> openNextPage(p,page));
        }
        mi = m.getItem("prev-page");
        if (mi != null) {
            mi.addAction(e -> openPrevPage(p,page));
        }
        p.openInventory(m.getInventory());
    }

    public void openNextPage(Player p, int page) {
        if (hasNextPage(p,page)) {
            open(p,page+1);
        }
    }

    private boolean hasNextPage(Player p, int page) {
        List<Reward> list = crate.getPossibleRewards(p);
        return (page < list.size()/rewardSlots.size());
    }

    public void openPrevPage(Player p, int page) {
        if (hasPreviousPage(page)) {
            open(p,page-1);
        }
    }

    private boolean hasPreviousPage(int page) {
        return page > 0;
    }

    public boolean isOpenableByKey() {
        return openableByKey;
    }

    public void loadRewardItems(Player p, Menu.Builder mb, int page) {
        List<Reward> rewards = crate.getPossibleRewards(p);

        int i = page * rewardSlots.size();
        for (int slot : rewardSlots) {
            if (i >= rewards.size()) {
                return;
            }
            Reward r = rewards.get(i);
            mb.addItem(MenuItem.builder("reward-"+r.getIdentifier(),r.getItem().getItem())
                    .slots(Arrays.asList(slot))
                    .build()
            );
            i++;
        }
    }

}
