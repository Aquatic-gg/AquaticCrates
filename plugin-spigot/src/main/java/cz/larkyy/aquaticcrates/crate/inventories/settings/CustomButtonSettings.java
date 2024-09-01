package cz.larkyy.aquaticcrates.crate.inventories.settings;

import gg.aquatic.aquaticseries.lib.action.ConfiguredAction;
import gg.aquatic.aquaticseries.lib.betterinventory2.SlotSelection;
import gg.aquatic.aquaticseries.lib.item.CustomItem;
import org.bukkit.entity.Player;

import java.util.List;

public class CustomButtonSettings {

    private final String id;
    private final CustomItem item;
    private final List<ConfiguredAction<Player>> configuredActions;
    private final SlotSelection slots;

    public CustomButtonSettings(String id, CustomItem item, List<ConfiguredAction<Player>> configuredActions, SlotSelection slots) {
        this.id = id;
        this.item = item;
        this.configuredActions = configuredActions;
        this.slots = slots;
    }

    public CustomItem getItem() {
        return item;
    }

    public List<ConfiguredAction<Player>> getConfiguredActions() {
        return configuredActions;
    }

    public SlotSelection getSlots() {
        return slots;
    }

    public String getId() {
        return id;
    }
}
