package cz.larkyy.aquaticcratestesting.loader;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.loader.impl.ItemsAdderLoader;
import cz.larkyy.aquaticcratestesting.loader.impl.ModelEngineLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class LoaderManager {

    private final List<Loader> loaders = new ArrayList<>();
    private final Runnable runnable;

    public LoaderManager(Runnable runnable) {
        this.runnable = runnable;
        if (getPlugin("ItemsAdder") != null) {
            loaders.add(new ItemsAdderLoader(
                    this::tryLoad
            ));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"iareload");
        }
        if (getPlugin("ModelEngine") != null) {
            loaders.add(new ModelEngineLoader(
                    this::tryLoad
            ));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"meg reload");
        }
    }

    public void tryLoad() {
        for (Loader loader : loaders) {
            if (!loader.isLoaded()) {
                return;
            }
        }
        load();
    }

    private void load() {
        runnable.run();
    }

    private Plugin getPlugin(String str) {
        return AquaticCratesTesting.instance().getServer().getPluginManager().getPlugin(str);
    }

}
