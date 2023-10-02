package xyz.larkyy.aquaticcrates.meg4hook;

import com.ticxo.modelengine.api.events.ModelRegistrationEvent;
import com.ticxo.modelengine.api.generator.ModelGenerator;
import cz.larkyy.aquaticcratestesting.nms.Loader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ModelEngineLoader extends Loader implements Listener {


    public ModelEngineLoader(Runnable runnable) {
        super(runnable);
    }

    @EventHandler
    public void onModelsLoad(ModelRegistrationEvent e) {
        if (e.getPhase().equals(ModelGenerator.Phase.FINISHED)) {
            setLoaded(true);
            getRunnable().run();
        }
    }
}
