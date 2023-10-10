package cz.larkyy.aquaticcratestesting.crate;

import cz.larkyy.aquaticcratestesting.hologram.Hologram;
import cz.larkyy.aquaticcratestesting.hologram.impl.AquaticHologram;
import cz.larkyy.aquaticcratestesting.model.Model;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
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
    private final Model model;
    private final Hologram hologram;

    public PlacedMultiCrate(MultiCrate multiCrate, Location location) {
        this.location = location;
        this.multiCrate = multiCrate;
        this.hologram = new AquaticHologram(location.clone().add(0,multiCrate.getHologramYOffset(),0),multiCrate.getHologram());
        hologram.spawn(new ArrayList<>(Bukkit.getOnlinePlayers()), list -> {});
        this.model = Model.create(multiCrate.getModel(), location,null,null);

        placedCrates = createPlacedCrates();
    }

    private Map<String,PlacedCrate> createPlacedCrates() {
        Map<String,PlacedCrate> map = new HashMap<>();
        for (String id : multiCrate.getCrates()) {
            Crate c = Crate.get(id);
            if (c == null) continue;
            map.put(id,new PlacedCrate(c,location,model,hologram));
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

    public Model getModel() {
        return model;
    }

    public void destroy() {
        model.remove();
        location.getBlock().setType(Material.AIR);
        hologram.despawn();
    }

    public void open(CratePlayer player, String crate, boolean instant) {
        var c = placedCrates.get(crate);
        if (c == null) return;
        c.getCrate().open(player,c,instant);
    }
}
