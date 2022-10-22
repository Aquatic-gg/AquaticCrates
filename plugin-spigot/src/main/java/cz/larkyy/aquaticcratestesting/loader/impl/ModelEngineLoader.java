package cz.larkyy.aquaticcratestesting.loader.impl;

import com.ticxo.modelengine.api.events.ModelRegistrationEvent;
import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.loader.Loader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ModelEngineLoader extends Loader implements Listener {


    public ModelEngineLoader(Runnable runnable) {
        super(runnable);
        AquaticCratesTesting.instance().getServer().getPluginManager().registerEvents(this,AquaticCratesTesting.instance());
    }

    @EventHandler
    public void onModelsLoad(ModelRegistrationEvent e) {
        if (e.getPhase().equals(ModelRegistrationEvent.Phase.FINAL)) {
            setLoaded(true);
            getRunnable().run();
        }
    }
}
