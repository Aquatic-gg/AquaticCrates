package cz.larkyy.aquaticcrates.crate.inventories.settings;

import gg.aquatic.aquaticseries.lib.inventory.lib.component.Button;

import java.util.List;

public class PreviewGUISettings {

    private final CustomInventorySettings settings;
    private final List<Integer> rewardSlots;
    private final Button milestoneItem;
    private final String milestoneFormat;
    private final String milestoneReachedFormat;
    private final Button repeatableMilestoneItem;
    private final String repeatableMilestoneFormat;
    private final List<String> rewardLore;
    private final boolean openableByKey;
    private final boolean clearBottomInventory;

    public PreviewGUISettings(CustomInventorySettings settings, List<Integer> rewardSlots, Button milestoneItem, String milestoneFormat, String milestoneReachedFormat, Button repeatableMilestoneItem, String repeatableMilestoneFormat, List<String> rewardLore, boolean openableByKey, boolean clearBottomInventory) {
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

    public Button getMilestoneItem() {
        return milestoneItem;
    }

    public boolean isClearBottomInventory() {
        return clearBottomInventory;
    }

    public CustomInventorySettings getSettings() {
        return settings;
    }

    public Button getRepeatableMilestoneItem() {
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
