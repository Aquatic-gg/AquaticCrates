package cz.larkyy.aquaticcratestesting.crate;

import cz.larkyy.aquaticcratestesting.api.AquaticCratesAPI;
import cz.larkyy.aquaticcratestesting.model.Model;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import org.bukkit.Location;
import org.bukkit.Material;

public class PlacedCrate {

    private final Location location;
    private final Crate crate;
    private final Model model;

    public PlacedCrate(Crate crate, Location location, String model) {
        this.location = location;
        this.crate = crate;
        this.model = Model.create(model,location);
        location.getBlock().setType(Material.BARRIER);
    }

    public static PlacedCrate get(Location location) {
        if (location == null) return null;
        return AquaticCratesAPI.getCrateHandler().getPlacedCrate(location);
    }

    public Location getLocation() {
        return location;
    }

    public Crate getCrate() {
        return crate;
    }

    public Model getModel() {
        return model;
    }

    public boolean open(CratePlayer player, boolean instant) {
        return crate.open(player,this,instant);
    }
}
