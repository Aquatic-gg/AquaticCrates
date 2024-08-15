package cz.larkyy.aquaticcrates.crate.inventories.settings;

public class MultiPreviewGUISettings {

    private final CustomInventorySettings settings;
    private final boolean clearBottomInventory;

    public MultiPreviewGUISettings(CustomInventorySettings settings, boolean clearBottomInventory) {
        this.settings = settings;
        this.clearBottomInventory = clearBottomInventory;
    }

    public CustomInventorySettings getSettings() {
        return settings;
    }

    public boolean isClearBottomInventory() {
        return clearBottomInventory;
    }
}
