package cz.larkyy.aquaticcrates.crate.inventories;

import cz.larkyy.aquaticcrates.crate.Crate;
import cz.larkyy.aquaticcrates.crate.PlacedCrate;
import cz.larkyy.aquaticcrates.crate.reward.Reward;
import cz.larkyy.aquaticcrates.player.CratePlayer;
import cz.larkyy.aquaticcrates.utils.Utils;
import gg.aquatic.aquaticseries.lib.AquaticSeriesLib;
import gg.aquatic.aquaticseries.lib.ItemStackExtKt;
import gg.aquatic.aquaticseries.lib.StringExtKt;
import gg.aquatic.aquaticseries.lib.action.ConfiguredAction;
import gg.aquatic.aquaticseries.lib.adapt.AquaticString;
import gg.aquatic.aquaticseries.lib.betterinventory2.AquaticInventory;
import gg.aquatic.aquaticseries.lib.betterinventory2.SlotSelection;
import gg.aquatic.aquaticseries.lib.betterinventory2.component.ButtonComponent;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholder;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholders;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class PreviewGUI {

    private final Crate crate;
    private final AquaticInventory inventory;
    private final Player player;

    public PreviewGUI(Crate crate, Player player) {
        this.crate = crate;
        this.player = player;
        inventory = new AquaticInventory(
                StringExtKt.toAquatic(Utils.updatePlaceholders(crate.getPreviewGUISettings().getSettings().getTitle(), player)),
                crate.getPreviewGUISettings().getSettings().getSize(),
                null,
                (pl,inv) -> {},
                (pl,inv) -> {},
                (pl,inv) -> {}
        );
    }

    private void addItems(Integer page, PlacedCrate placedCrate) {
        for (var entry : crate.getPreviewGUISettings().getSettings().getButtons().entrySet()) {
            var id = entry.getKey();
            var button = entry.getValue();
            switch (id.toLowerCase()) {
                case "next-page" -> {
                    var newButton = new ButtonComponent(
                            id,
                            1,
                            button.getSlots(),
                            new HashMap<>(),
                            null,
                            onClick -> {
                                for (ConfiguredAction<Player> configuredAction : button.getConfiguredActions()) {
                                    configuredAction.run(player, new Placeholders());
                                }
                                openNextPage(player, placedCrate, page);
                                onClick.setCancelled(true);
                            },
                            5,
                            (p, str) -> str,
                            button.getItem().getItem()
                    );
                    inventory.addComponent(newButton);
                    continue;
                }
                case "prev-page" -> {
                    var newButton = new ButtonComponent(
                            id,
                            1,
                            button.getSlots(),
                            new HashMap<>(),
                            null,
                            onClick -> {
                                for (ConfiguredAction<Player> configuredAction : button.getConfiguredActions()) {
                                    configuredAction.run(player, new Placeholders());
                                }
                                openPrevPage(player, placedCrate, page);
                                onClick.setCancelled(true);
                            },
                            5,
                            (p, str) -> str,
                            button.getItem().getItem()
                    );
                    inventory.addComponent(newButton);
                    continue;
                }
                case "open-button" -> {
                    var newButton = new ButtonComponent(
                            id,
                            1,
                            button.getSlots(),
                            new HashMap<>(),
                            null,
                            onClick -> {
                                for (ConfiguredAction<Player> configuredAction : button.getConfiguredActions()) {
                                    configuredAction.run(player, new Placeholders());
                                }
                                player.closeInventory();
                                crate.open(CratePlayer.get(player), placedCrate, false);
                                onClick.setCancelled(true);
                            },
                            5,
                            (p, str) -> str,
                            button.getItem().getItem()
                    );
                    inventory.addComponent(newButton);
                    continue;
                }
                case "close-button" -> {
                    var newButton = new ButtonComponent(
                            id,
                            1,
                            button.getSlots(),
                            new HashMap<>(),
                            null,
                            onClick -> {
                                for (ConfiguredAction<Player> configuredAction : button.getConfiguredActions()) {
                                    configuredAction.run(player, new Placeholders());
                                }
                                player.closeInventory();
                                onClick.setCancelled(true);
                            },
                            5,
                            (p, str) -> str,
                            button.getItem().getItem()
                    );
                    inventory.addComponent(newButton);
                    continue;
                }
                default -> {
                    var newButton = new ButtonComponent(
                            id,
                            1,
                            button.getSlots(),
                            new HashMap<>(),
                            null,
                            onClick -> {
                                for (ConfiguredAction<Player> configuredAction : button.getConfiguredActions()) {
                                    configuredAction.run(player, new Placeholders());
                                }
                                onClick.setCancelled(true);
                            },
                            5,
                            (p, str) -> PlaceholderAPI.setPlaceholders(player,str),
                            button.getItem().getItem()
                    );
                    inventory.addComponent(newButton);
                    continue;
                }
            }
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
            inventory.addComponent(button);
        }
    }

    /*
    private void addActionToButton(Button button, Consumer<ComponentClickEvent> consumer) {
        if (button.getOnClick() != null) {
            button.getOnClick().andThen(consumer);
        } else {
            button.setOnClick(consumer);
        }
    }

     */

    public void open(Player p, PlacedCrate placedCrate) {
        openPage(p, placedCrate, 0);
        inventory.open(p);
    }

    public void openPage(Player p, PlacedCrate placedCrate, int page) {
        inventory.clearComponents();
        addItems(page, placedCrate);
        loadRewardItems(p, page);
        loadMilestoneItem(p);
        loadRepetableMilestoneItem(p);
        inventory.updateComponents(player);
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
                ((player1, s) -> s),
                is
        );
        inventory.addComponent(newButton);
    }

    private void loadRepetableMilestoneItem(Player p) {
        var repeatableMilestoneItem = crate.getPreviewGUISettings().getRepeatableMilestoneItem();
        if (repeatableMilestoneItem == null) {
            return;
        }
        var milestoneHandler = crate.getMilestoneHandler();
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
                ((player1, s) -> s),
                is
        );
        inventory.addComponent(newButton);
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
            lore.replaceAll(s ->
                    s.replace("%chance%", r.chance() + "")
            );
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
                    10000,
                    ((player1, s) -> s),
                    is
            );
            inventory.addComponent(newButton);
            i++;
        }
    }
}
