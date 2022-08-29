package cz.larkyy.aquaticcratestesting.model.impl;

import cz.larkyy.aquaticcratestesting.model.Model;
import org.bukkit.Location;

public class EmptyModel extends Model {

    private final Location location;
    public EmptyModel(Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }
}
