package cz.larkyy.aquaticcratestesting.crate;

import cz.larkyy.aquaticcratestesting.crate.model.ModelSettings;
import org.bukkit.Material;

import java.util.List;

public class CrateBase {
    private final String identifier;
    private final String displayName;
    private final List<String> hologram;
    private final double hologramYOffset;
    private Material blockType = Material.BARRIER;
    private final int hitboxHeight;
    private final int hitboxWidth;
    private final ModelSettings modelSettings;

    public CrateBase(String identifier, String displayName, ModelSettings modelSettings, List<String> hologram, double hologramYOffset, int hitboxHeight, int hitboxWidth) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.modelSettings = modelSettings;
        this.hologram = hologram;
        this.hologramYOffset = hologramYOffset;
        this.hitboxHeight = hitboxHeight;
        this.hitboxWidth = hitboxWidth;
    }

    public double getHologramYOffset() {
        return hologramYOffset;
    }

    public List<String> getHologram() {
        return hologram;
    }

    public Material getBlockType() {
        return blockType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ModelSettings getModelSettings() {
        return modelSettings;
    }

    public String getModel() {
        return modelSettings.getModelId();
    }

    public void setBlockType(Material blockType) {
        this.blockType = blockType;
    }

    public int getHitboxHeight() {
        return hitboxHeight;
    }

    public int getHitboxWidth() {
        return hitboxWidth;
    }
}
