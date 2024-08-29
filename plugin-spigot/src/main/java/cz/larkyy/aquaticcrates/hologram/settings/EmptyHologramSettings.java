package cz.larkyy.aquaticcrates.hologram.settings;

import cz.larkyy.aquaticcrates.hologram.Hologram;
import cz.larkyy.aquaticcrates.hologram.impl.EmptyHologram;
import org.bukkit.Location;

public class EmptyHologramSettings implements HologramSettings{
    @Override
    public Hologram create(Location location) {
        return new EmptyHologram(location);
    }
}
