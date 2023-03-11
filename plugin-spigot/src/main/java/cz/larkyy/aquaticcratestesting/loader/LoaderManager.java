package cz.larkyy.aquaticcratestesting.loader;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.loader.impl.AquaticEngineLoader;
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
        if (getPlugin("AquaticModelEngine") != null) {
            try {
                Class.forName("xyz.larkyy.aquaticmodelengine.api.event.ModelLoadEvent");
                Class.forName("xyz.larkyy.aquaticmodelengine.api.event.State");
                var loader = new AquaticEngineLoader(
                        this::tryLoad
                );
                loaders.add(loader);
                if (AquaticCratesTesting.loaded) {
                    loader.load();
                }
            } catch (ClassNotFoundException ignored) {
            }
        }
        if (getPlugin("ItemsAdder") != null) {
            loaders.add(new ItemsAdderLoader(
                    this::tryLoad
            ));
            if (AquaticCratesTesting.loaded) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "iareload");
            }
        }
        if (getPlugin("ModelEngine") != null) {
            try {
                Class.forName("com.ticxo.modelengine.api.events.ModelRegistrationEvent");

                loaders.add(new ModelEngineLoader(
                        this::tryLoad
                ));
                if (AquaticCratesTesting.loaded) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "meg reload");
                }
            } catch (ClassNotFoundException ignored) {
                Bukkit.getConsoleSender().sendMessage("[AquaticCrates] Using an old version of ModelEngine! Use at least v3.0.0!");

            }
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
