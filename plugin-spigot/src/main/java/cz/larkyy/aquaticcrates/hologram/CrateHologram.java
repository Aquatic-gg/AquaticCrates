package cz.larkyy.aquaticcrates.hologram;

import gg.aquatic.aquaticseries.lib.betterhologram.AquaticHologram;
import org.bukkit.util.Vector;

import java.util.List;

public class CrateHologram {

    private final Hologram hologram;
    private final Vector offset;


    public CrateHologram(Hologram hologram, Vector offset) {
        this.hologram = hologram;
        this.offset = offset;
    }
}
