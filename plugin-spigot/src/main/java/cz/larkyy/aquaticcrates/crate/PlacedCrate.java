package cz.larkyy.aquaticcrates.crate;

import cz.larkyy.aquaticcrates.api.AquaticCratesAPI;
import cz.larkyy.aquaticcrates.hologram.Hologram;
import cz.larkyy.aquaticcrates.player.CratePlayer;
import gg.aquatic.aquaticseries.lib.audience.GlobalAudience;
import gg.aquatic.aquaticseries.lib.interactable2.SpawnedInteractable;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class PlacedCrate {

    private final Location location;
    private final Crate crate;
    //private final Model model;
    private final Hologram hologram;
    //private final ModelAnimationHandler modelAnimationHandler;
    private final SpawnedInteractable<?> spawnedInteractable;

    public PlacedCrate(Crate crate, Location location) {
        this.location = location;
        this.crate = crate;
        this.hologram = crate.getHologram().create(location);
        hologram.spawn(new GlobalAudience(), list -> {});
        spawnedInteractable = crate.getInteractable().spawn(location, false, true);
    }
    public PlacedCrate(Crate crate, Location location, SpawnedInteractable<?> spawnedInteractable, Hologram hologram) {
        this.location = location;
        this.crate = crate;
        this.hologram = hologram;
        this.spawnedInteractable = spawnedInteractable;
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

    public void destroy() {
        for (Location hitboxLocation : getHitboxLocations()) {
            hitboxLocation.getBlock().setType(Material.AIR);
        }

        hologram.despawn();
        spawnedInteractable.despawn();
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

    public SpawnedInteractable<?> getSpawnedInteractable() {
        return spawnedInteractable;
    }
}
