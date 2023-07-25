package cz.larkyy.aquaticcratestesting.crate;

import cz.larkyy.aquaticcratestesting.api.AquaticCratesAPI;
import cz.larkyy.aquaticcratestesting.hologram.Hologram;
import cz.larkyy.aquaticcratestesting.hologram.impl.AquaticHologram;
import cz.larkyy.aquaticcratestesting.model.Model;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;

public class PlacedCrate {

    private final Location location;
    private final Crate crate;
    private final Model model;
    private final Hologram hologram;

    public PlacedCrate(Crate crate, Location location, String model) {
        this.location = location;
        this.crate = crate;
        this.hologram = new AquaticHologram(location.clone().add(0,crate.getHologramYOffset(),0),crate.getHologram());
        hologram.spawn(new ArrayList<>(Bukkit.getOnlinePlayers()), list -> {});
        this.model = Model.create(model,location,null);
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

    public void destroy() {
        model.remove();
        location.getBlock().setType(Material.AIR);
        hologram.despawn();
    }

    public boolean open(CratePlayer player, boolean instant) {
        return crate.open(player,this,instant);
    }

    public Hologram getHologram() {
        return hologram;
    }
}
