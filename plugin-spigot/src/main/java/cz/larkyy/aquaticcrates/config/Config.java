package cz.larkyy.aquaticcrates.config;

import cz.larkyy.aquaticcrates.crate.inventories.settings.CustomButtonSettings;
import cz.larkyy.aquaticcrates.hologram.settings.AquaticHologramSettings;
import cz.larkyy.aquaticcrates.hologram.settings.EmptyHologramSettings;
import cz.larkyy.aquaticcrates.hologram.settings.HologramSettings;
import gg.aquatic.aquaticseries.lib.ConfigExtKt;
import gg.aquatic.aquaticseries.lib.action.player.PlayerActionSerializer;
import gg.aquatic.aquaticseries.lib.betterhologram.AquaticHologram;
import gg.aquatic.aquaticseries.lib.betterhologram.HologramSerializer;
import gg.aquatic.aquaticseries.lib.betterhologram.impl.ArmorstandLine;
import gg.aquatic.aquaticseries.lib.betterhologram.impl.TextDisplayLine;
import gg.aquatic.aquaticseries.lib.betterinventory2.SlotSelection;
import gg.aquatic.aquaticseries.lib.betterinventory2.serialize.ButtonSettings;
import gg.aquatic.aquaticseries.lib.betterinventory2.serialize.InventorySerializer;
import gg.aquatic.aquaticseries.lib.item.CustomItem;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private final File file;
    private FileConfiguration config;
    private final JavaPlugin main;

    public Config(JavaPlugin main, String path) {
        this.main = main;
        this.file = new File(main.getDataFolder(), path);
    }

    public Config(JavaPlugin main, File file) {
        this.main = main;
        this.file = file;
    }

    public void load() {
        if (!this.file.exists()) {
            try {
                this.main.saveResource(this.file.getName(), false);
            } catch (IllegalArgumentException var4) {
                try {
                    this.file.createNewFile();
                } catch (IOException var3) {
                    var3.printStackTrace();
                }
            }
        }

        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration getConfiguration() {
        if (this.config == null) {
            this.load();
        }
        return this.config;
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

    ////////////////////////////////////////////////////////////////////////////

    protected HologramSettings loadHologram(String path) {
        if (!getConfiguration().isConfigurationSection(path)) {
            return new EmptyHologramSettings();
        }
        var section = getConfiguration().getConfigurationSection(path);
        var type = section.getString("type", "empty").toLowerCase();
        switch (type) {
            case "aquatic" -> {
                return loadAquaticHologram(path);
            }
            default -> {
                return new EmptyHologramSettings();
            }
        }
    }

    protected AquaticHologramSettings loadAquaticHologram(String path) {
        if (!getConfiguration().isConfigurationSection(path)) return new AquaticHologramSettings(
                new ArrayList<>(),
                new Vector(0, 0, 0),
                AquaticHologram.Billboard.CENTER
        );
        var section = getConfiguration().getConfigurationSection(path);
        var offset = section.getString("offset", "0;0;0").split(";");
        var billboard = AquaticHologram.Billboard.valueOf(section.getString("billboard", "CENTER").toUpperCase());
        var vector = new Vector(
                Double.parseDouble(offset[0]),
                Double.parseDouble(offset[1]),
                Double.parseDouble(offset[2])
        );
        var lines = new ArrayList<>(HologramSerializer.INSTANCE.load(ConfigExtKt.getSectionList(section, "lines")));
        for (AquaticHologram.Line line : lines) {
            if (line instanceof TextDisplayLine) {
                ((TextDisplayLine) line).setTextUpdater((PlaceholderAPI::setPlaceholders
                ));
            } else if (line instanceof ArmorstandLine) {
                ((ArmorstandLine) line).setTextUpdater((PlaceholderAPI::setPlaceholders
                ));
            }
        }
        return new AquaticHologramSettings(lines, vector, billboard);
    }

    /*
    protected Map<String, CustomButtonSettings> loadInventoryButtons(ConfigurationSection section) {
        Map<String, CustomButtonSettings> buttons = new HashMap<>();
        if (section == null) return buttons;
        for (String key : section.getKeys(false)) {
            var buttonSection = section.getConfigurationSection(key);
            assert buttonSection != null;
            var button = loadButton(buttonSection, key);
            //buttons.put(key, button);
        }
        return buttons;
    }
     */

    protected ButtonSettings loadButton(String path, String id) {
        if (!getConfiguration().contains(path)) {
            return null;
        }
        return loadButton(getConfiguration().getConfigurationSection(path), id);
    }

    protected ButtonSettings loadButton(ConfigurationSection section, String id) {
        if (section == null) {
            return null;
        }

        var button = InventorySerializer.INSTANCE.loadButton(section, id);
        return button;
    }
}
