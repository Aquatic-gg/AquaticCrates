package cz.larkyy.aquaticcrates.hologram.settings;

import cz.larkyy.aquaticcrates.hologram.Hologram;
import cz.larkyy.aquaticcrates.hologram.impl.aquatic.AHologram;
import gg.aquatic.aquaticseries.lib.betterhologram.AquaticHologram;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

public class AquaticHologramSettings implements HologramSettings {

    private final List<AquaticHologram.Line> lines;
    private final Vector offset;
    private final AquaticHologram.Billboard billboard;

    public AquaticHologramSettings(List<AquaticHologram.Line> lines, Vector offset, AquaticHologram.Billboard billboard) {
        this.lines = lines;
        this.offset = offset;
        this.billboard = billboard;
    }

    @Override
    public Hologram create(Location location) {
        var hologram = new AHologram(
                location.clone().add(offset),
                this
        );
        return hologram;
    }

    public List<AquaticHologram.Line> getLines() {
        return lines;
    }

    public Vector getOffset() {
        return offset;
    }

    public AquaticHologram.Billboard getBillboard() {
        return billboard;
    }
}
