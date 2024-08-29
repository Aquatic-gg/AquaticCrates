package cz.larkyy.aquaticcrates.crate;

import cz.larkyy.aquaticcrates.crate.model.ModelSettings;
import cz.larkyy.aquaticcrates.hologram.settings.HologramSettings;
import gg.aquatic.aquaticseries.lib.adapt.AquaticString;
import gg.aquatic.aquaticseries.lib.interactable2.AbstractInteractable;
import org.bukkit.Material;

public class CrateBase {
    private final String identifier;
    private final AquaticString displayName;
    private final HologramSettings hologram;
    private Material blockType = Material.BARRIER;
    private final int hitboxHeight;
    private final int hitboxWidth;
    private final ModelSettings modelSettings;
    private final AbstractInteractable<?> interactable;

    public CrateBase(String identifier, AquaticString displayName, ModelSettings modelSettings, HologramSettings hologram, int hitboxHeight, int hitboxWidth, AbstractInteractable<?> interactable) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.modelSettings = modelSettings;
        this.hologram = hologram;
        this.hitboxHeight = hitboxHeight;
        this.hitboxWidth = hitboxWidth;
        this.interactable = interactable;
    }

    public HologramSettings getHologram() {
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
