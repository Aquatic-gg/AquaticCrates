package cz.larkyy.aquaticcratestesting.crate;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.config.CrateConfig;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class CrateHandler {

    private final Map<String,Crate> crates;
    private final Map<Location, PlacedCrate> locations;

    public CrateHandler() {
        crates = new HashMap<>();
        locations = new HashMap<>();
    }

    public void addCrate(Crate crate) {
        crates.put(crate.getIdentifier(),crate);
    }


    public Crate getCrate(String identifier) {
        return crates.get(identifier);
    }

    public PlacedCrate spawnCrate(Location location, Crate crate) {
        PlacedCrate pc = new PlacedCrate(crate,location,crate.getModel());
        locations.put(location,pc);
        return pc;
    }

    public PlacedCrate getPlacedCrate(Location location) {
        return locations.get(location);
    }

    public void load() {
        loadCrates();
    }

    private void loadCrates() {
        File cratesFolder = new File(AquaticCratesTesting.instance().getDataFolder(),"crates/");
        cratesFolder.mkdirs();

        for (File file : cratesFolder.listFiles()) {
            Crate crate = new CrateConfig(AquaticCratesTesting.instance(),file).loadCrate();
            crates.put(crate.getIdentifier(),crate);
        }
    }
}
