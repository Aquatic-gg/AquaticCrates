package cz.larkyy.aquaticcrates.crate.inventories.settings;

import gg.aquatic.aquaticseries.lib.betterinventory2.serialize.InventorySettings;

public class MultiPreviewGUISettings {

    private final InventorySettings settings;
    private final boolean clearBottomInventory;

    public MultiPreviewGUISettings(InventorySettings settings, boolean clearBottomInventory) {
        this.settings = settings;
        this.clearBottomInventory = clearBottomInventory;
    }

    public InventorySettings getSettings() {
        return settings;
    }

    public boolean isClearBottomInventory() {
        return clearBottomInventory;
    }
}
