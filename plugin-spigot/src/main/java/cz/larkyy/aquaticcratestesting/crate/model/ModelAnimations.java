package cz.larkyy.aquaticcratestesting.crate.model;

import java.util.List;
import java.util.Random;

public class ModelAnimations {

    private static final Random RANDOM = new Random();
    private final List<ModelAnimation> animations;
    private final int period;

    public ModelAnimations(List<ModelAnimation> animations, int period) {
        this.animations = animations;
        this.period = period;
    }

    public List<ModelAnimation> getAnimations() {
        return animations;
    }

    public int getPeriod() {
        return period;
    }

    public ModelAnimation getRandomAnimation() {
        if (animations.isEmpty()) return null;
        return animations.get(RANDOM.nextInt(animations.size()));
    }
}
