package cz.larkyy.aquaticcratestesting.messages;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.config.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class MessageHandler {
    private final Config config = new Config(AquaticCratesTesting.instance(),"messages.yml");

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
