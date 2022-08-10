package cz.larkyy.aquaticcratestesting.config;

import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.item.CustomItem;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CrateConfig extends Config{

    private final String identifier;

    public CrateConfig(JavaPlugin main, File f) {
        super(main, f);
        this.identifier = f.getName().substring(0,f.getName().length()-4);
    }

    public Crate loadCrate() {
        return new Crate(
                identifier,
                loadKey(),
                getConfiguration().getString("model")
        );
    }

    private CustomItem loadKey() {
        return loadItem("key");
    }

    private CustomItem loadItem(String path) {
        return CustomItem.create(
                getConfiguration().getString(path+".material"),
                getConfiguration().getString(path+".display-name"),
                getConfiguration().getStringList(path+".lore")
                );
    }
}
