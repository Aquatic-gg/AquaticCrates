package cz.larkyy.aquaticcrates.hologram;

import gg.aquatic.aquaticseries.lib.audience.AquaticAudience;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public abstract class Hologram {

    private Location location;

    public Hologram(Location location) {
        this.location = location;
    }

    public abstract void move(Location location);

    public abstract void despawn();

    public abstract void spawn(AquaticAudience audience, Consumer<List<String>> consumer);

    public abstract void update(Consumer<List<String>> consumer);


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
