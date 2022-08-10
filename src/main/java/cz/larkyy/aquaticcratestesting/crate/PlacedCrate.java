package cz.larkyy.aquaticcratestesting.crate;

import cz.larkyy.aquaticcratestesting.model.Model;
import org.bukkit.Location;

public class PlacedCrate {

    private final Location location;
    private final Crate crate;
    private final Model model;

    public PlacedCrate(Crate crate, Location location, String model) {
        this.location = location;
        this.crate = crate;
        this.model = Model.create(model,location);
    }

}
