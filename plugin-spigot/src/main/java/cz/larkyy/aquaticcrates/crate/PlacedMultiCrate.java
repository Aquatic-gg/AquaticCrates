package cz.larkyy.aquaticcrates.crate;

import cz.larkyy.aquaticcrates.crate.model.ModelAnimationHandler;
import cz.larkyy.aquaticcrates.hologram.Hologram;
import cz.larkyy.aquaticcrates.hologram.impl.AquaticHologram;
import cz.larkyy.aquaticcrates.model.Model;
import cz.larkyy.aquaticcrates.player.CratePlayer;
import gg.aquatic.aquaticseries.lib.interactable2.AbstractSpawnedPacketInteractable;
import gg.aquatic.aquaticseries.lib.interactable2.SpawnedInteractable;
import gg.aquatic.aquaticseries.lib.interactable2.impl.block.SpawnedPacketBlockInteractable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlacedMultiCrate {

    private final MultiCrate multiCrate;
    private final Location location;
    private final Map<String,PlacedCrate> placedCrates;
    private final SpawnedInteractable<?> spawnedInteractable;
    private final Hologram hologram;
    private final ModelAnimationHandler modelAnimationHandler;

    public PlacedMultiCrate(MultiCrate multiCrate, Location location) {
        this.location = location;
        this.multiCrate = multiCrate;
        this.hologram = new AquaticHologram(location.clone().add(0,multiCrate.getHologramYOffset(),0),multiCrate.getHologram());
        hologram.spawn(new ArrayList<>(Bukkit.getOnlinePlayers()), list -> {});
        spawnedInteractable = multiCrate.getInteractable().spawn(location, false);
        //this.model = Model.create(multiCrate.getModel(), location,null,null);

        placedCrates = createPlacedCrates();
        this.modelAnimationHandler = new ModelAnimationHandler(spawnedInteractable,multiCrate);
    }

    private Map<String,PlacedCrate> createPlacedCrates() {
        Map<String,PlacedCrate> map = new HashMap<>();
        for (String id : multiCrate.getCrates()) {
            Crate c = Crate.get(id);
            if (c == null) continue;
            map.put(id,new PlacedCrate(c,location,spawnedInteractable,hologram));
        }
        return map;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public MultiCrate getMultiCrate() {
        return multiCrate;
    }

    public Map<String, PlacedCrate> getPlacedCrates() {
        return placedCrates;
    }

    public Location getLocation() {
        return location;
    }

    public SpawnedInteractable<?> getModel() {
        return spawnedInteractable;
    }

    public void destroy() {
        modelAnimationHandler.setCancelled(true);
        spawnedInteractable.despawn();
        location.getBlock().setType(Material.AIR);
        hologram.despawn();
    }

    public void open(CratePlayer player, String crate, boolean instant) {
        var c = placedCrates.get(crate);
        if (c == null) return;
        c.getCrate().open(player,c,instant);
    }
}
