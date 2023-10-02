package cz.larkyy.aquaticcratestesting.loader;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.loader.impl.ItemsAdderLoader;
import xyz.larkyy.aquaticcrates.meg3hook.ModelEngineLoader;
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
            if (AquaticCratesTesting.loaded) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "iareload");
            }
        }
        if (getPlugin("ModelEngine") != null) {
            if (AquaticCratesTesting.getModelEngineAdapter() != null)
            {
                loaders.add(AquaticCratesTesting.getModelEngineAdapter().createMEGLoader(AquaticCratesTesting.instance(),
                        this::tryLoad
                ));
                if (AquaticCratesTesting.loaded) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "meg reload");
                }
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
        return AquaticCratesTesting.instance().getServer().getPluginManager().getPlugin(str);
    }

}
