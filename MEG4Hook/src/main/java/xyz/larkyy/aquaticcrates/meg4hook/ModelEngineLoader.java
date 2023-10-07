package xyz.larkyy.aquaticcrates.meg4hook;

import com.ticxo.modelengine.api.events.ModelRegistrationEvent;
import com.ticxo.modelengine.api.generator.ModelGenerator;
import cz.larkyy.aquaticcratestesting.nms.Loader;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

public class ModelEngineLoader extends Loader {


    public ModelEngineLoader(Runnable runnable) {
        super(runnable);
    }

    @EventHandler
    public void onModelsLoad(ModelRegistrationEvent e) {
        if (e.getPhase().equals(ModelGenerator.Phase.POST_IMPORT)) {
            setLoaded(true);
            getRunnable().run();
        }
    }
}
