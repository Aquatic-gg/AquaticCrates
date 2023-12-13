package cz.larkyy.aquaticcratestesting.crate;

import cz.larkyy.aquaticcratestesting.api.AquaticCratesAPI;
import cz.larkyy.aquaticcratestesting.crate.model.ModelAnimationHandler;
import cz.larkyy.aquaticcratestesting.hologram.Hologram;
import cz.larkyy.aquaticcratestesting.hologram.impl.AquaticHologram;
import cz.larkyy.aquaticcratestesting.model.Model;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class PlacedCrate {

    private final Location location;
    private final Crate crate;
    private final Model model;
    private final Hologram hologram;
    private final ModelAnimationHandler modelAnimationHandler;

    public PlacedCrate(Crate crate, Location location) {
        this.location = location;
        this.crate = crate;
        this.hologram = new AquaticHologram(location.clone().add(0,crate.getHologramYOffset(),0),crate.getHologram());
        hologram.spawn(new ArrayList<>(Bukkit.getOnlinePlayers()), list -> {});
        this.model = Model.create(crate.getModel(), location,null,null);
        this.modelAnimationHandler = new ModelAnimationHandler(model,crate);
    }
    public PlacedCrate(Crate crate, Location location, Model model, Hologram hologram) {
        this.location = location;
        this.crate = crate;
        this.hologram = hologram;
        this.model = model;
        this.modelAnimationHandler = new ModelAnimationHandler(model,crate);
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
        modelAnimationHandler.setCancelled(true);
        model.remove();

        for (Location hitboxLocation : getHitboxLocations()) {
            hitboxLocation.getBlock().setType(Material.AIR);
        }

        hologram.despawn();
    }

    public List<Location> getHitboxLocations() {
        List<Location> locations = new ArrayList<>();
        var loc = location.clone();
        for (int x = -(getCrate().getHitboxWidth()-1); x < getCrate().getHitboxWidth(); x++) {
            for (int z = -(getCrate().getHitboxWidth()-1); z < getCrate().getHitboxWidth(); z++) {
                for (int height = 0; height < getCrate().getHitboxHeight(); height++) {
                    locations.add(loc.clone().add(x, height, z).getBlock().getLocation());
                }
            }
        }
        return locations;
    }

    public void open(CratePlayer player, boolean instant) {
        crate.open(player,this,instant);
    }

    public Hologram getHologram() {
        return hologram;
    }
}
