package cz.larkyy.aquaticcrates.crate.inventories.settings;

import gg.aquatic.aquaticseries.lib.betterinventory2.serialize.ButtonSettings;
import gg.aquatic.aquaticseries.lib.betterinventory2.serialize.InventorySettings;

import java.util.List;

public class PreviewGUISettings {

    private final InventorySettings settings;
    private final List<Integer> rewardSlots;
    private final ButtonSettings milestoneItem;
    private final String milestoneFormat;
    private final String milestoneReachedFormat;
    private final ButtonSettings repeatableMilestoneItem;
    private final String repeatableMilestoneFormat;
    private final List<String> rewardLore;
    private final boolean openableByKey;
    private final boolean clearBottomInventory;

    public PreviewGUISettings(InventorySettings settings, List<Integer> rewardSlots, ButtonSettings milestoneItem, String milestoneFormat, String milestoneReachedFormat, ButtonSettings repeatableMilestoneItem, String repeatableMilestoneFormat, List<String> rewardLore, boolean openableByKey, boolean clearBottomInventory) {
        this.settings = settings;
        this.rewardSlots = rewardSlots;
        this.milestoneItem = milestoneItem;
        this.milestoneFormat = milestoneFormat;
        this.milestoneReachedFormat = milestoneReachedFormat;
        this.repeatableMilestoneItem = repeatableMilestoneItem;
        this.repeatableMilestoneFormat = repeatableMilestoneFormat;
        this.rewardLore = rewardLore;
        this.openableByKey = openableByKey;
        this.clearBottomInventory = clearBottomInventory;
    }

    public ButtonSettings getMilestoneItem() {
        return milestoneItem;
    }

    public boolean isClearBottomInventory() {
        return clearBottomInventory;
    }

    public InventorySettings getSettings() {
        return settings;
    }

    public ButtonSettings getRepeatableMilestoneItem() {
        return repeatableMilestoneItem;
    }

    public List<Integer> getRewardSlots() {
        return rewardSlots;
    }

    public String getMilestoneFormat() {
        return milestoneFormat;
    }

    public String getMilestoneReachedFormat() {
        return milestoneReachedFormat;
    }

    public String getRepeatableMilestoneFormat() {
        return repeatableMilestoneFormat;
    }

    public boolean isOpenableByKey() {
        return openableByKey;
    }

    public List<String> getRewardLore() {
        return rewardLore;
    }
}
