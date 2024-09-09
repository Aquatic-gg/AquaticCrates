package cz.larkyy.aquaticcrates.crate.inventories;

import cz.larkyy.aquaticcrates.crate.Crate;
import cz.larkyy.aquaticcrates.crate.PlacedCrate;
import cz.larkyy.aquaticcrates.crate.reward.Reward;
import cz.larkyy.aquaticcrates.player.CratePlayer;
import gg.aquatic.aquaticseries.lib.AquaticSeriesLib;
import gg.aquatic.aquaticseries.lib.ItemStackExtKt;
import gg.aquatic.aquaticseries.lib.StringExtKt;
import gg.aquatic.aquaticseries.lib.adapt.AquaticString;
import gg.aquatic.aquaticseries.lib.betterinventory2.AquaticInventory;
import gg.aquatic.aquaticseries.lib.betterinventory2.SlotSelection;
import gg.aquatic.aquaticseries.lib.betterinventory2.component.ButtonComponent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PreviewGUI extends AquaticInventory {

    private final Crate crate;
    private final Player player;

    public PreviewGUI(Crate crate, Player player) {
        super(
                crate.getPreviewGUISettings().getSettings().getTitle(),
                crate.getPreviewGUISettings().getSettings().getSize(),
                crate.getPreviewGUISettings().getSettings().getInventoryType(),
                (player1, aquaticInventory) -> {},
                (player1, aquaticInventory) -> {},
                (inventoryInteractEvent, aquaticInventory) -> {

                }
        );
        this.crate = crate;
        this.player = player;
    }

    private void addItems(Integer page, PlacedCrate placedCrate) {
        for (var buttonSettings : crate.getPreviewGUISettings().getSettings().getButtons()) {
            var id = buttonSettings.getId();
            var button = buttonSettings.create((p, str) -> PlaceholderAPI.setPlaceholders(player,str), e -> {
                switch (id.toLowerCase()) {
                    case "next-page" -> {
                        if (hasNextPage(player, page)) {
                            openNextPage(player, placedCrate, page);
                        }
                        e.setCancelled(true);
                    }
                    case "prev-page" -> {
                        if (page != 0) {
                            openPrevPage(player, placedCrate, page);
                        }
                        e.setCancelled(true);
                    }
                    case "open-button" -> {
                        e.setCancelled(true);
                        player.closeInventory();
                        crate.open(CratePlayer.get(player), placedCrate, false);
                    }
                    case "close-button" -> {
                        player.closeInventory();
                        e.setCancelled(true);
                    }
                    default -> {
                        e.setCancelled(true);
                    }
                }
            });
            addComponent(button);
        }
        if (crate.getPreviewGUISettings().isClearBottomInventory()) {
            var button = new ButtonComponent(
                    "clear-item-component",
                    -1,
                    SlotSelection.Companion.rangeOf(crate.getPreviewGUISettings().getSettings().getSize(), crate.getPreviewGUISettings().getSettings().getSize() + 36),
                    new HashMap<>(),
                    null,
                    e -> {
                        e.setCancelled(true);
                    },
                    10000,
                    (p, str) -> str,
                    new ItemStack(Material.AIR)
            );
            addComponent(button);
        }
    }

    public void open(Player p, PlacedCrate placedCrate) {
        openPage(p, placedCrate, 0);
        open(p);
    }

    public void openPage(Player p, PlacedCrate placedCrate, int page) {
        clearComponents();
        addItems(page, placedCrate);
        loadRewardItems(p, page);
        loadMilestoneItem(p);
        loadRepetableMilestoneItem(p);
        updateComponents(player);
        //inventory.getComponentHandler().redrawComponents();
    }

    public void openNextPage(Player p, PlacedCrate placedCrate, int page) {
        if (hasNextPage(p, page)) {
            openPage(p, placedCrate, page + 1);
        }
    }

    private boolean hasNextPage(Player p, int page) {
        List<Reward> list = crate.getPossibleRewards(p);
        return (page < list.size() / crate.getPreviewGUISettings().getRewardSlots().size());
    }

    public void openPrevPage(Player p, PlacedCrate placedCrate, int page) {
        if (hasPreviousPage(page)) {
            openPage(p, placedCrate, page - 1);
        }
    }

    private boolean hasPreviousPage(int page) {
        return page > 0;
    }

    public boolean isOpenableByKey() {
        return crate.getPreviewGUISettings().isOpenableByKey();
    }

    private void loadMilestoneItem(Player p) {
        var milestoneItem = crate.getPreviewGUISettings().getMilestoneItem();
        if (milestoneItem == null) {
            return;
        }
        var milestoneHandler = crate.getMilestoneHandler();
        /*
        var is = milestoneItem.getItem().getItem().clone();
        var im = is.getItemMeta();

        List<String> newLore = new ArrayList<>();
        for (String line : im.getLore()) {
            if (!line.toLowerCase().contains("%milestones%")) {
                newLore.add(line);
                continue;
            }
            milestoneHandler.getMilestones().forEach((i, milestone) -> {
                var placeholders = new Placeholders();
                placeholders.addPlaceholder(new Placeholder("%milestone%", milestone.getDisplayName().getString()));
                var reached = milestoneHandler.getAmt(p);

                if (reached < milestone.getMilestone()) {
                    placeholders.addPlaceholder(new Placeholder("%remains%", (milestone.getMilestone() - reached) + ""));
                    placeholders.addPlaceholder(new Placeholder("%reached%", reached + ""));
                    placeholders.addPlaceholder(new Placeholder("%required%", milestone.getMilestone() + ""));
                    newLore.add(placeholders.replace(crate.getPreviewGUISettings().getMilestoneFormat()));
                } else {
                    newLore.add(placeholders.replace(crate.getPreviewGUISettings().getMilestoneReachedFormat()));
                }
            });
        }
        ItemStackExtKt.lore(im, StringExtKt.toAquatic(newLore));
        //im.setLore(newLore);
        is.setItemMeta(im);
        var newButton = new ButtonComponent(
                "milestone",
                1,
                milestoneItem.getSlots(),
                new HashMap<>(),
                null,
                onClick -> {
                    for (ConfiguredAction<Player> configuredAction : milestoneItem.getConfiguredActions()) {
                        configuredAction.run(player, new Placeholders());
                    }
                    onClick.setCancelled(true);
                },
                10000,
                (player1, str) -> PlaceholderAPI.setPlaceholders(player,str),
                is
        );
        inventory.addComponent(newButton);
         */
        addComponent(milestoneItem.create(
                (player1, s) -> s, e -> e.setCancelled(true)
        ));
    }

    private void loadRepetableMilestoneItem(Player p) {
        var repeatableMilestoneItem = crate.getPreviewGUISettings().getRepeatableMilestoneItem();
        if (repeatableMilestoneItem == null) {
            return;
        }
        var milestoneHandler = crate.getMilestoneHandler();
        /*
        var is = repeatableMilestoneItem.getItem().getItem().clone();
        var im = is.getItemMeta();

        List<String> newLore = new ArrayList<>();
        for (String line : im.getLore()) {
            if (!line.toLowerCase().contains("%milestones%")) {
                newLore.add(line);
                continue;
            }
            milestoneHandler.getRepeatableMilestones().forEach((i, milestone) -> {
                var placeholders = new Placeholders();
                placeholders.addPlaceholder(new Placeholder("%milestone%", milestone.getDisplayName().getString()));

                var current = milestoneHandler.getAmt(p);

                double d1 = (double) current / (double) milestone.getMilestone();
                double d2 = Math.floor(d1);

                int reached = (int) ((d1 - d2) * milestone.getMilestone());

                placeholders.addPlaceholder(new Placeholder("%remains%", (milestone.getMilestone() - reached) + ""));
                placeholders.addPlaceholder(new Placeholder("%reached%", reached + ""));
                placeholders.addPlaceholder(new Placeholder("%required%", milestone.getMilestone() + ""));
                newLore.add(placeholders.replace(crate.getPreviewGUISettings().getRepeatableMilestoneFormat()));
            });
        }
        ItemStackExtKt.lore(im, StringExtKt.toAquatic(newLore));
        is.setItemMeta(im);
        var newButton = new ButtonComponent(
                "repeatable-milestone",
                1,
                repeatableMilestoneItem.getSlots(),
                new HashMap<>(),
                null,
                onClick -> {
                    for (ConfiguredAction<Player> configuredAction : repeatableMilestoneItem.getConfiguredActions()) {
                        configuredAction.run(player, new Placeholders());
                    }
                    onClick.setCancelled(true);
                },
                10000,
                (player1, str) -> PlaceholderAPI.setPlaceholders(player,str),
                is
        );
        inventory.addComponent(newButton);
         */

        addComponent(repeatableMilestoneItem.create(
                (player1, s) -> s, e -> e.setCancelled(true)
        ));
    }

    public void loadRewardItems(Player p, int page) {
        List<Reward> rewards = crate.getPossibleRewards(p);
        var rewardSlots = crate.getPreviewGUISettings().getRewardSlots();
        var rewardLore = crate.getPreviewGUISettings().getRewardLore();

        int i = page * rewardSlots.size();
        for (int slot : rewardSlots) {
            if (i >= rewards.size()) {
                return;
            }
            Reward r = rewards.get(i);
            ItemStack is = r.getItem().getItem();
            ItemMeta im = is.getItemMeta();
            List<AquaticString> lore = new ArrayList<>(AquaticSeriesLib.Companion.getINSTANCE().getAdapter().getItemStackAdapter().getAquaticLore(im));

            lore.addAll(StringExtKt.toAquatic(rewardLore));
            ItemStackExtKt.lore(im, lore);
            is.setItemMeta(im);

            var newButton = new ButtonComponent(
                    "reward-" + r.getIdentifier(),
                    1,
                    SlotSelection.Companion.of(slot),
                    new HashMap<>(),
                    null,
                    e -> {
                        e.setCancelled(true);
                        if (!(e.getWhoClicked() instanceof Player pl)) {
                            return;
                        }
                        if (pl.hasPermission("aquaticcrates.admin")) {
                            r.give(player);
                        }
                    },
                    5,
                    (player1, str) ->
                            PlaceholderAPI.setPlaceholders(player,str)
                                    .replace("%chance%", r.chance() + "")
                    ,
                    is
            );
            addComponent(newButton);
            i++;
        }
    }
}
