package cz.larkyy.aquaticcratestesting.crate.model;

public class ModelSettings {

    private final String modelId;
    private final ModelAnimations modelAnimations;

    public ModelSettings(String modelId, ModelAnimations modelAnimations) {
        this.modelAnimations = modelAnimations;
        this.modelId = modelId;
    }

    public String getModelId() {
        return modelId;
    }

    public ModelAnimations getModelAnimations() {
        return modelAnimations;
    }
}
