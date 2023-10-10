package cz.larkyy.aquaticcratestesting.crate;

import org.bukkit.Material;

import java.util.List;

public class CrateBase {
    private final String identifier;
    private final String displayName;
    private final String model;
    private final List<String> hologram;
    private final double hologramYOffset;
    private Material blockType = Material.BARRIER;

    public CrateBase(String identifier, String displayName, String model, List<String> hologram, double hologramYOffset) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.model = model;
        this.hologram = hologram;
        this.hologramYOffset = hologramYOffset;
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

    public String getModel() {
        return model;
    }

    public void setBlockType(Material blockType) {
        this.blockType = blockType;
    }
}
