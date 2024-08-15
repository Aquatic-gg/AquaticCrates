package cz.larkyy.aquaticcrates.crate.inventories.settings;

import gg.aquatic.aquaticseries.lib.inventory.lib.component.Button;

import java.util.Map;

public class CustomInventorySettings {

    private final String title;
    private final Integer size;
    private final Map<String,Button> buttons;

    public CustomInventorySettings(String title, Integer size, Map<String,Button> buttons) {
        this.title = title;
        this.size = size;
        this.buttons = buttons;
    }

    public Map<String,Button> getButtons() {
        return buttons;
    }

    public Integer getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }
}
