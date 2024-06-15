package cz.larkyy.aquaticcrates.crate.model;

public class ModelAnimation {
    private final String animationId;
    private final int animationLength;

    public ModelAnimation(String animationId, int animationLength) {
        this.animationId = animationId;
        this.animationLength = animationLength;
    }

    public String getAnimationId() {
        return animationId;
    }

    public int getAnimationLength() {
        return animationLength;
    }
}
