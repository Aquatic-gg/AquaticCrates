package cz.larkyy.aquaticcrates.crate.inventories.settings;

import java.util.Map;

public class CustomInventorySettings {

    private final String title;
    private final Integer size;
    private final Map<String,CustomButtonSettings> buttons;

    public CustomInventorySettings(String title, Integer size, Map<String,CustomButtonSettings> buttons) {
        this.title = title;
        this.size = size;
        this.buttons = buttons;
    }

    public Map<String,CustomButtonSettings> getButtons() {
        return buttons;
    }

    public Integer getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }
}
