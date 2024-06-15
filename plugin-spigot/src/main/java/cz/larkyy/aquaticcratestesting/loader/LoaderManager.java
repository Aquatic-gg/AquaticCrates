package cz.larkyy.aquaticcratestesting.loader;

import cz.larkyy.aquaticcratestesting.AquaticCrates;
import cz.larkyy.aquaticcratestesting.loader.impl.ItemsAdderLoader;
import cz.larkyy.aquaticcratestesting.nms.Loader;
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
            if (AquaticCrates.loaded) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "iareload");
            }
        }
        if (AquaticCrates.getModelEngineAdapter() != null) {
            var loader = AquaticCrates.getModelEngineAdapter().createMEGLoader(AquaticCrates.instance(),
                    this::tryLoad
            );
            loaders.add(loader);
            if (AquaticCrates.loaded) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "meg reload");
            }
        }
        if (loaders.isEmpty()) {
            tryLoad();
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
        return AquaticCrates.instance().getServer().getPluginManager().getPlugin(str);
    }

}
