package cz.larkyy.aquaticcrates.crate.inventories;

import cz.larkyy.aquaticcrates.crate.Crate;
import cz.larkyy.aquaticcrates.crate.PlacedCrate;
import cz.larkyy.aquaticcrates.crate.reward.Reward;
import cz.larkyy.aquaticcrates.player.CratePlayer;
import cz.larkyy.aquaticcrates.utils.Utils;
import gg.aquatic.aquaticseries.lib.AquaticSeriesLib;
import gg.aquatic.aquaticseries.lib.ItemStackExtKt;
import gg.aquatic.aquaticseries.lib.StringExtKt;
import gg.aquatic.aquaticseries.lib.adapt.AquaticString;
import gg.aquatic.aquaticseries.lib.inventory.lib.SlotSelection;
import gg.aquatic.aquaticseries.lib.inventory.lib.component.Button;
import gg.aquatic.aquaticseries.lib.inventory.lib.event.ComponentClickEvent;
import gg.aquatic.aquaticseries.lib.inventory.lib.inventory.PersonalizedInventory;
import gg.aquatic.aquaticseries.lib.inventory.lib.title.TitleHolder;
import gg.aquatic.aquaticseries.lib.inventory.lib.title.component.BasicTitleComponent;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholder;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PreviewGUI extends PersonalizedInventory {

    private final Crate crate;

    public PreviewGUI(Crate crate, Player player) {
        super(TitleHolder.Companion.of(
                new BasicTitleComponent(StringExtKt.toAquatic(Utils.updatePlaceholders(crate.getPreviewGUISettings().getSettings().getTitle(), player)))
        ), crate.getPreviewGUISettings().getSettings().getSize(), player, factory -> {
        });
        this.crate = crate;
    }

    private void addItems(Integer page, PlacedCrate placedCrate) {
        for (var entry : crate.getPreviewGUISettings().getSettings().getButtons().entrySet()) {
            var id = entry.getKey();
            var button = entry.getValue();
            switch (id.toLowerCase()) {
                case "next-page" -> {
                    var newButton = new Button(
                            button.getItemStack(),
                            button.getSlotSelection(),
                            onClick -> {
                                var previousAction = button.getOnClick();
                                if (previousAction != null) {
                                    previousAction.accept(onClick);
                                }
                                openNextPage(getPlayer(), placedCrate, page);
                                onClick.getOriginalEvent().setCancelled(true);
                            },
                            button.getPriority()
                    );
                    this.getComponentHandler().getComponents().put(id, newButton);
                    continue;
                }
                case "prev-page" -> {
                    var newButton = new Button(
                            button.getItemStack(),
                            button.getSlotSelection(),
                            onClick -> {
                                var previousAction = button.getOnClick();
                                if (previousAction != null) {
                                    previousAction.accept(onClick);
                                }
                                openPrevPage(getPlayer(), placedCrate, page);
                                onClick.getOriginalEvent().setCancelled(true);
                            },
                            button.getPriority()
                    );
                    this.getComponentHandler().getComponents().put(id, newButton);
                    continue;
                }
                case "open-button" -> {
                    var newButton = new Button(
                            button.getItemStack(),
                            button.getSlotSelection(),
                            onClick -> {
                                var previousAction = button.getOnClick();
                                if (previousAction != null) {
                                    previousAction.accept(onClick);
                                }
                                getPlayer().closeInventory();
                                crate.open(CratePlayer.get(getPlayer()), placedCrate, false);
                                onClick.getOriginalEvent().setCancelled(true);
                            },
                            button.getPriority()
                    );
                    this.getComponentHandler().getComponents().put(id, newButton);
                    continue;
                }
                case "close-button" -> {
                    var newButton = new Button(
                            button.getItemStack(),
                            button.getSlotSelection(),
                            onClick -> {
                                var previousAction = button.getOnClick();
                                if (previousAction != null) {
                                    previousAction.accept(onClick);
                                }
                                getPlayer().closeInventory();
                                onClick.getOriginalEvent().setCancelled(true);
                            },
                            button.getPriority()
                    );
                    this.getComponentHandler().getComponents().put(id, newButton);
                    continue;
                }
                default -> {

                }
            }
            this.getComponentHandler().getComponents().put(id, button);
        }
        if (crate.getPreviewGUISettings().isClearBottomInventory()) {
            for (int i = 0; i < crate.getPreviewGUISettings().getSettings().getSize() + 36 - 1; i++) {
                var button = new Button(
                        new ItemStack(Material.AIR),
                        SlotSelection.Companion.of(i),
                        e -> {
                            e.getOriginalEvent().setCancelled(true);
                        },
                        0
                );
                getComponentHandler().getComponents().put("clear-item-" + i, button);
            }
        }
    }

    private void addActionToButton(Button button, Consumer<ComponentClickEvent> consumer) {
        if (button.getOnClick() != null) {
            button.getOnClick().andThen(consumer);
        } else {
            button.setOnClick(consumer);
        }
    }

    public void open(Player p, PlacedCrate placedCrate) {
        openPage(p, placedCrate, 0);
        open(true);
    }

    public void openPage(Player p, PlacedCrate placedCrate, int page) {
        getComponentHandler().getComponents().clone();
        addItems(page, placedCrate);
        loadRewardItems(p, page);
        loadMilestoneItem(p);
        loadRepetableMilestoneItem(p);
        redrawComponents();
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
        var newButton = new Button(
                is,
                milestoneItem.getSlotSelection(),
                milestoneItem.getOnClick(),
                1
        );
        getComponentHandler().getComponents().put("milestone", newButton);
    }

    private void loadRepetableMilestoneItem(Player p) {
        var repeatableMilestoneItem = crate.getPreviewGUISettings().getRepeatableMilestoneItem();
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
        var newButton = new Button(
                is,
                repeatableMilestoneItem.getSlotSelection(),
                repeatableMilestoneItem.getOnClick(),
                1
        );
        getComponentHandler().getComponents().put("repeatable-milestone", newButton);
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

            var newButton = new Button(
                    is,
                    SlotSelection.Companion.of(slot),
                    e -> {
                        e.getOriginalEvent().setCancelled(true);
                        if (!(e.getOriginalEvent().getWhoClicked() instanceof Player player)) {
                            return;
                        }
                        if (player.hasPermission("aquaticcrates.admin")) {
                            r.give(player);
                        }
                    },
                    1
            );
            getComponentHandler().getComponents().put("reward-" + r.getIdentifier(), newButton);
            i++;
        }
    }
}
