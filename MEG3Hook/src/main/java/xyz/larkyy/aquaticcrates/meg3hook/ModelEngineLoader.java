package xyz.larkyy.aquaticcrates.meg3hook;

import com.ticxo.modelengine.api.events.ModelRegistrationEvent;
import cz.larkyy.aquaticcratestesting.nms.Loader;
import org.bukkit.event.EventHandler;

public class ModelEngineLoader extends Loader {

    public ModelEngineLoader(Runnable runnable) {
        super(runnable);
    }

    @EventHandler
    public void onModelsLoad(ModelRegistrationEvent e) {
        if (e.getPhase().equals(ModelRegistrationEvent.Phase.FINAL)) {
            setLoaded(true);
            getRunnable().run();
        }
    }
}
