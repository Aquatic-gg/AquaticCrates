package cz.larkyy.aquaticcratestesting.crate;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.config.Config;
import cz.larkyy.aquaticcratestesting.config.CrateConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class CrateHandler {

    private final Map<String,Crate> crates;
    private final Map<Location, PlacedCrate> locations;

    private static final Config crateData = new Config(AquaticCratesTesting.instance(),"locations.yml");

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
        Location loc = location.clone().toBlockLocation();
        loc.setYaw(0);
        locations.put(loc,pc);
        return pc;
    }

    public PlacedCrate getPlacedCrate(Location location) {
        return locations.get(location);
    }

    public void load() {
        loadCrates();
    }

    private void loadCrates() {
        crateData.load();
        File cratesFolder = new File(AquaticCratesTesting.instance().getDataFolder(),"crates/");
        cratesFolder.mkdirs();

        for (File file : cratesFolder.listFiles()) {
            Crate crate = new CrateConfig(AquaticCratesTesting.instance(),file).loadCrate();
            crates.put(crate.getIdentifier(),crate);
        }

        if (!crateData.getConfiguration().contains("crates")) {
            return;
        }
        for (String s : crateData.getConfiguration().getStringList("crates")) {
            String[] strs = s.split("\\|");
            Crate crate = Crate.get(strs[0]);
            if (crate == null) {
                continue;
            }
            World w = Bukkit.getWorld(strs[1]);
            if (w == null) {
                continue;
            }
            double x = Double.parseDouble(strs[2]);
            double y = Double.parseDouble(strs[3]);
            double z = Double.parseDouble(strs[4]);
            float yaw = Float.parseFloat(strs[5]);
            spawnCrate(new Location(w,x,y,z,yaw,0),crate);
        }
    }

    public void saveCrates() {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<String> strs = new ArrayList<>();
                for (Map.Entry<Location, PlacedCrate> entry : locations.entrySet()) {
                    PlacedCrate pc = entry.getValue();
                    Location l = pc.getLocation();

                    strs.add(pc.getCrate().getIdentifier()+"|"+l.getWorld().getName()+"|"+l.getX()+"|"+l.getY()+"|"+l.getZ()+"|"+l.getYaw());
                }
                crateData.getConfiguration().set("crates",strs);
                crateData.save();
            }
        }.runTaskAsynchronously(AquaticCratesTesting.instance());
    }
}
