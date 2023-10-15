package cz.larkyy.aquaticcratestesting.animation.showcase;

import cz.larkyy.aquaticcratestesting.model.Model;
import org.bukkit.Location;

public class ModelRewardShowcase implements RewardShowcase{

    private final Model model;

    public ModelRewardShowcase(Model model) {
        this.model = model;
    }

    public Model getModel() {
        return model;
    }

    @Override
    public Location getLocation() {
        return model.getLocation();
    }

    @Override
    public void destroy() {
        model.remove();
    }
}
