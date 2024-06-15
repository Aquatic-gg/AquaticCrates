package cz.larkyy.aquaticcrates.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public abstract class Hologram {

    private Location location;
    private List<String> lines;

    public Hologram(Location location, List<String> lines) {
        this.location = location;
        this.lines = lines;
    }

    public abstract void teleport(Location location);
    public abstract void move(Location location);

    public abstract void despawn();

    public abstract void spawn(List<Player> visitors, Consumer<List<String>> consumer);

    public abstract void hide();
    public abstract void show();

    public abstract void update(Consumer<List<String>> consumer);


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }
}
