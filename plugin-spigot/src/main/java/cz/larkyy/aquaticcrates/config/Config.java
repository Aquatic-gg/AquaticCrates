package cz.larkyy.aquaticcrates.config;

import cz.larkyy.aquaticcrates.hologram.settings.AquaticHologramSettings;
import cz.larkyy.aquaticcrates.hologram.settings.EmptyHologramSettings;
import cz.larkyy.aquaticcrates.hologram.settings.HologramSettings;
import gg.aquatic.aquaticseries.lib.betterhologram.AquaticHologram;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Config {
    private final File file;
    private FileConfiguration config;
    private final JavaPlugin main;

    public Config(JavaPlugin main, String path) {
        this.main = main;
        this.file = new File(main.getDataFolder(), path);
    }

    public Config(JavaPlugin main,File file) {
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
        if (!getConfiguration().contains(path)) {
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
        if (!getConfiguration().contains(path)) return new AquaticHologramSettings(
                new ArrayList<>(),
                new Vector(0, 0, 0)
        );
        var section = getConfiguration().getConfigurationSection(path);
        var offset = section.getString("offset", "0;0;0").split(";");
        var vector = new Vector(
                Double.parseDouble(offset[0]),
                Double.parseDouble(offset[1]),
                Double.parseDouble(offset[2])
        );
        var lines = new ArrayList<AquaticHologram.Line>();
        // TODO: Line serialization

        return new AquaticHologramSettings(lines, vector);
    }
}
