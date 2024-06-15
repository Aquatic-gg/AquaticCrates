package cz.larkyy.aquaticcrates.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

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
}
