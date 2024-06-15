package cz.larkyy.aquaticcrates.messages;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.config.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class MessageHandler {
    private final Config config = new Config(AquaticCrates.instance(),"messages.yml");

    public void load() {
        config.load();
    }

    public FileConfiguration getCfg() {
        return config.getConfiguration();
    }

    public void save() {
        config.save();
    }
}
