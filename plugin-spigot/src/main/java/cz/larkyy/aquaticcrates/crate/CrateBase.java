package cz.larkyy.aquaticcrates.crate;

import cz.larkyy.aquaticcrates.crate.model.ModelSettings;
import gg.aquatic.aquaticseries.lib.adapt.AquaticString;
import gg.aquatic.aquaticseries.lib.interactable2.AbstractInteractable;
import org.bukkit.Material;

import java.util.List;

public class CrateBase {
    private final String identifier;
    private final AquaticString displayName;
    private final List<String> hologram;
    private final double hologramYOffset;
    private Material blockType = Material.BARRIER;
    private final int hitboxHeight;
    private final int hitboxWidth;
    private final ModelSettings modelSettings;
    private final AbstractInteractable<?> interactable;

    public CrateBase(String identifier, AquaticString displayName, ModelSettings modelSettings, List<String> hologram, double hologramYOffset, int hitboxHeight, int hitboxWidth, AbstractInteractable<?> interactable) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.modelSettings = modelSettings;
        this.hologram = hologram;
        this.hologramYOffset = hologramYOffset;
        this.hitboxHeight = hitboxHeight;
        this.hitboxWidth = hitboxWidth;
        this.interactable = interactable;
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

    public AquaticString getDisplayName() {
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

    public AbstractInteractable<?> getInteractable() {
        return interactable;
    }
}
