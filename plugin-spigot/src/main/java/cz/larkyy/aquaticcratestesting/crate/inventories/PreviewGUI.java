package cz.larkyy.aquaticcratestesting.crate.inventories;

import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.crate.PlacedCrate;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import cz.larkyy.aquaticcratestesting.placeholders.Placeholder;
import cz.larkyy.aquaticcratestesting.placeholders.Placeholders;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import cz.larkyy.aquaticcratestesting.utils.colors.Colors;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.larkyy.menulib.Menu;
import xyz.larkyy.menulib.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreviewGUI {

    private final Crate crate;
    private final List<Integer> rewardSlots;
    private final MenuItem milestoneItem;
    private final String milestoneFormat;
    private final String milestoneReachedFormat;
    private final MenuItem repeatableMilestoneItem;
    private final String repeatableMilestoneFormat;
    private final Menu.Builder mb;
    private final List<String> rewardLore;
    private final boolean openableByKey;

    public PreviewGUI(Crate crate, Menu.Builder mb, List<Integer> rewardSlots, List<String> rewardLore, boolean openableByKey,
                      MenuItem milestoneItem, String milestoneFormat, String milestoneReachedFormat, MenuItem repeatableMilestoneItem,
                      String repeatableMilestoneFormat) {
        this.crate = crate;
        this.rewardSlots = rewardSlots;
        this.mb = mb;
        this.openableByKey = openableByKey;
        this.rewardLore = rewardLore;
        this.milestoneItem = milestoneItem;
        this.milestoneFormat = milestoneFormat;
        this.milestoneReachedFormat = milestoneReachedFormat;
        this.repeatableMilestoneItem = repeatableMilestoneItem;
        this.repeatableMilestoneFormat = repeatableMilestoneFormat;
    }


    public void open(Player p, int page, PlacedCrate pc) {
        Menu.Builder builder = mb.clone();
        loadRewardItems(p, builder, page);
        loadMilestoneItem(p, builder);
        loadRepetableMilestoneItem(p, builder);
        Menu m = builder.build();

        MenuItem mi;
        mi = m.getItem("next-page");
        if (mi != null) {
            mi.addAction(e -> openNextPage(p, page, pc));
        }
        mi = m.getItem("prev-page");
        if (mi != null) {
            mi.addAction(e -> openPrevPage(p, page, pc));
        }
        mi = m.getItem("open-button");
        if (mi != null) {
            mi.addAction(e -> {
                p.closeInventory();
                crate.open(CratePlayer.get(p), pc, false);
            });
        }
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
            im.setLore(PlaceholderAPI.setPlaceholders(p.getPlayer(), im.getLore()));
            is.setItemMeta(im);
        }

        p.openInventory(m.getInventory());
    }

    public void openNextPage(Player p, int page, PlacedCrate pc) {
        if (hasNextPage(p, page)) {
            open(p, page + 1, pc);
        }
    }

    private boolean hasNextPage(Player p, int page) {
        List<Reward> list = crate.getPossibleRewards(p);
        return (page < list.size() / rewardSlots.size());
    }

    public void openPrevPage(Player p, int page, PlacedCrate pc) {
        if (hasPreviousPage(page)) {
            open(p, page - 1, pc);
        }
    }

    private boolean hasPreviousPage(int page) {
        return page > 0;
    }

    public boolean isOpenableByKey() {
        return openableByKey;
    }

    private void loadMilestoneItem(Player p, Menu.Builder mb) {
        if (milestoneItem == null) {
            return;
        }
        var milestoneHandler = crate.getMilestoneHandler();
        var is = milestoneItem.getItemStack().clone();
        var im = is.getItemMeta();

        List<String> newLore = new ArrayList<>();
        for (String line : im.getLore()) {
            if (!line.toLowerCase().contains("%milestones%")) {
                newLore.add(line);
                continue;
            }
            milestoneHandler.getMilestones().forEach((i, milestone) -> {
                var placeholders = new Placeholders();
                placeholders.addPlaceholder("%milestone%", milestone.getDisplayName());
                var reached = milestoneHandler.getAmt(p);

                if (reached < milestone.getMilestone()) {
                    placeholders.addPlaceholder("%remains%", (milestone.getMilestone() - reached) + "");
                    placeholders.addPlaceholder("%reached%", reached + "");
                    placeholders.addPlaceholder("%required%", milestone.getMilestone() + "");
                    newLore.add(Colors.format(placeholders.replace(milestoneFormat)));
                } else {
                    newLore.add(Colors.format(placeholders.replace(milestoneReachedFormat)));
                }
            });
        }
        im.setLore(newLore);
        is.setItemMeta(im);
        mb.addItem(MenuItem.builder("milestones", is).action(milestoneItem::activate).slots(milestoneItem.getSlots()).build());
    }

    private void loadRepetableMilestoneItem(Player p, Menu.Builder mb) {
        if (repeatableMilestoneItem == null) {
            return;
        }
        var milestoneHandler = crate.getMilestoneHandler();
        var is = repeatableMilestoneItem.getItemStack().clone();
        var im = is.getItemMeta();

        List<String> newLore = new ArrayList<>();
        for (String line : im.getLore()) {
            if (!line.toLowerCase().contains("%milestones%")) {
                newLore.add(line);
                continue;
            }
            milestoneHandler.getRepeatableMilestones().forEach((i, milestone) -> {
                var placeholders = new Placeholders();
                placeholders.addPlaceholder("%milestone%", milestone.getDisplayName());

                var current = milestoneHandler.getAmt(p);

                double d1 = (double)current/(double)milestone.getMilestone();
                double d2 = Math.floor(d1);

                int reached = (int) ((d1-d2)*milestone.getMilestone());

                placeholders.addPlaceholder("%remains%", (milestone.getMilestone() - reached) + "");
                placeholders.addPlaceholder("%reached%", reached + "");
                placeholders.addPlaceholder("%required%", milestone.getMilestone() + "");
                newLore.add(Colors.format(placeholders.replace(repeatableMilestoneFormat)));
            });
        }
        im.setLore(newLore);
        is.setItemMeta(im);
        mb.addItem(MenuItem.builder("repeatable-milestones", is).action(repeatableMilestoneItem::activate).slots(repeatableMilestoneItem.getSlots()).build());
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
                    s.replace("%chance%", r.getChance() + "")
            );
            im.setLore(lore);
            is.setItemMeta(im);

            mb.addItem(MenuItem.builder("reward-" + r.getIdentifier(), is)
                    .slots(Arrays.asList(slot))
                    .action(e -> {
                        if (!(e.getWhoClicked() instanceof Player player)) {
                            return;
                        }
                        if (player.hasPermission("aquaticcrates.admin")) {
                            r.give(player);
                        }
                    })
                    .build()
            );
            i++;
        }
    }
}
