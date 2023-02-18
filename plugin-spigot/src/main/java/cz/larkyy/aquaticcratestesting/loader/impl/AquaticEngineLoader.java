package cz.larkyy.aquaticcratestesting.loader.impl;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.loader.Loader;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.larkyy.aquaticmodelengine.api.event.ModelLoadEvent;
import xyz.larkyy.aquaticmodelengine.api.event.State;

public class AquaticEngineLoader extends Loader implements Listener {

    public AquaticEngineLoader(Runnable runnable) {
        super(runnable);
        AquaticCratesTesting.instance().getServer().getPluginManager().registerEvents(this,AquaticCratesTesting.instance());
    }

    @EventHandler
    public void onAMEModelsLoad(ModelLoadEvent e) {
        Bukkit.broadcastMessage("Listened!");
        if (e.getState().equals(State.FINISHED)) {
            setLoaded(true);
            getRunnable().run();
        }
    }
}
