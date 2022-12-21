package cz.larkyy.aquaticcratestesting.crate.inventories;

import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.crate.PlacedCrate;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.larkyy.colorutils.Colors;
import xyz.larkyy.menulib.Menu;
import xyz.larkyy.menulib.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreviewGUI {

    private final Crate crate;
    private final List<Integer> rewardSlots;
    private final Menu.Builder mb;
    private final List<String> rewardLore;
    private final boolean openableByKey;

    public PreviewGUI(Crate crate, Menu.Builder mb, List<Integer> rewardSlots, List<String> rewardLore, boolean openableByKey) {
        this.crate = crate;
        this.rewardSlots = rewardSlots;
        this.mb = mb;
        this.openableByKey = openableByKey;
        this.rewardLore = rewardLore;
    }

    public void open(Player p, int page, PlacedCrate pc) {
        Menu.Builder builder = mb.clone();
        loadRewardItems(p,builder,page);
        Menu m = builder.build();

        MenuItem mi;
        mi = m.getItem("next-page");
        if (mi != null) {
            mi.addAction(e -> openNextPage(p,page,pc));
        }
        mi = m.getItem("prev-page");
        if (mi != null) {
            mi.addAction(e -> openPrevPage(p,page,pc));
        }
        mi = m.getItem("open-button");
        if (mi != null) {
            mi.addAction(e -> {
                p.closeInventory();
                crate.open(CratePlayer.get(p),pc,false);
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

    public void openNextPage(Player p, int page, PlacedCrate pc) {
        if (hasNextPage(p,page)) {
            open(p,page+1, pc);
        }
    }

    private boolean hasNextPage(Player p, int page) {
        List<Reward> list = crate.getPossibleRewards(p);
        return (page < list.size()/rewardSlots.size());
    }

    public void openPrevPage(Player p, int page, PlacedCrate pc) {
        if (hasPreviousPage(page)) {
            open(p,page-1, pc);
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
            ItemStack is = r.getPreviewItem().getItem();
            ItemMeta im = is.getItemMeta();
            List<String> lore = new ArrayList<>();
            if (im.getLore() != null) {
                lore.addAll(im.getLore());
            }
            lore.addAll(Colors.format(rewardLore));
            lore.replaceAll(s ->
                    s.replace("%chance%",r.getChance()+"")
            );
            im.setLore(lore);
            is.setItemMeta(im);

            mb.addItem(MenuItem.builder("reward-"+r.getIdentifier(),is)
                    .slots(Arrays.asList(slot))
                    .build()
            );
            i++;
        }
    }
}
