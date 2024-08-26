package cz.larkyy.aquaticcrates.crate.inventories.settings;

import gg.aquatic.aquaticseries.lib.action.ConfiguredAction;
import gg.aquatic.aquaticseries.lib.item.CustomItem;

import java.util.List;

public class CustomButtonSettings {

    private final String id;
    private final CustomItem item;
    private final List<ConfiguredAction> configuredActions;
    private final List<Integer> slots;

    public CustomButtonSettings(String id, CustomItem item, List<ConfiguredAction> configuredActions, List<Integer> slots) {
        this.id = id;
        this.item = item;
        this.configuredActions = configuredActions;
        this.slots = slots;
    }

    public CustomItem getItem() {
        return item;
    }

    public List<ConfiguredAction> getConfiguredActions() {
        return configuredActions;
    }

    public List<Integer> getSlots() {
        return slots;
    }

    public String getId() {
        return id;
    }
}
