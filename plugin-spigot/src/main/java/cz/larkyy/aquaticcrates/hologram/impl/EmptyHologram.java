package cz.larkyy.aquaticcrates.hologram.impl;

import cz.larkyy.aquaticcrates.hologram.Hologram;
import gg.aquatic.aquaticseries.lib.audience.AquaticAudience;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public class EmptyHologram extends Hologram {
    public EmptyHologram(Location location) {
        super(location);
    }

    @Override
    public void move(Location location) {

    }

    @Override
    public void despawn() {

    }

    @Override
    public void spawn(AquaticAudience audience, Consumer<List<String>> consumer) {

    }

    @Override
    public void update(Consumer<List<String>> consumer) {

    }
}
