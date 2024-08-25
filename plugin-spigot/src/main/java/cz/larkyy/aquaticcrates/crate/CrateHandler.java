package cz.larkyy.aquaticcrates.crate;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.api.events.CratePlaceEvent;
import cz.larkyy.aquaticcrates.api.events.MultiCratePlaceEvent;
import cz.larkyy.aquaticcrates.config.Config;
import cz.larkyy.aquaticcrates.config.CrateConfig;
import cz.larkyy.aquaticcrates.config.MultiCrateConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class CrateHandler {

    private final Map<String,Crate> crates;
    private final Map<Location, PlacedCrate> locations;

    private final Map<String,MultiCrate> multiCrates;
    private final Map<Location, PlacedMultiCrate> multiLocations;

    private static final Config crateData = new Config(AquaticCrates.instance(),"locations.yml");

    public CrateHandler() {
        crates = new HashMap<>();
        locations = new HashMap<>();
        multiCrates = new HashMap<>();
        multiLocations = new HashMap<>();
    }

    public void removePlacedCrate(Location location) {
        PlacedCrate pc = locations.remove(location);
        if (pc == null) {
            return;
        }
        for (Location hitboxLocation : pc.getHitboxLocations()) {
            locations.remove(hitboxLocation);
        }
        pc.destroy();
        saveCrates();
    }
    public void removePlacedMultiCrate(Location location) {
        PlacedMultiCrate pc = multiLocations.remove(location);
        if (pc == null) {
            return;
        }
        pc.destroy();
        saveCrates();
    }

    public Crate getCrate(String identifier) {
        return crates.get(identifier);
    }
    public MultiCrate getMultiCrate(String identifier) {
        return multiCrates.get(identifier);
    }

    public PlacedCrate spawnCrate(Location location, Crate crate) {
        PlacedCrate pc = new PlacedCrate(crate,location);

        CratePlaceEvent event = new CratePlaceEvent(pc);
        Bukkit.getServer().getPluginManager().callEvent(event);

        /*
        Location loc = location.clone().getBlock().getLocation();

        loc.setYaw(0);
        for (Location hitboxLocation : pc.getHitboxLocations()) {
            hitboxLocation.getBlock().setType(event.getBlockMaterial());
            locations.put(hitboxLocation,pc);
        }
         */

        return pc;
    }
    public PlacedMultiCrate spawnMultiCrate(Location location, MultiCrate crate) {
        PlacedMultiCrate pc = new PlacedMultiCrate(crate,location);

        MultiCratePlaceEvent event = new MultiCratePlaceEvent(pc);
        Bukkit.getServer().getPluginManager().callEvent(event);
        Location loc = location.clone().getBlock().getLocation();
        loc.getBlock().setType(event.getBlockMaterial());
        loc.setYaw(0);
        multiLocations.put(loc,pc);
        return pc;
    }

    public PlacedCrate getPlacedCrate(Location location) {
        return locations.get(location);
    }
    public PlacedMultiCrate getPlacedMultiCrate(Location location) {
        return multiLocations.get(location);
    }

    public void load() {
        loadCrates();
    }

    private void loadCrates() {
        crateData.load();
        File cratesFolder = new File(AquaticCrates.instance().getDataFolder(),"crates/");
        cratesFolder.mkdirs();

        for (File file : cratesFolder.listFiles()) {
            Crate crate = new CrateConfig(AquaticCrates.instance(),file).loadCrate();
            if (crate != null) {
                crates.put(crate.getIdentifier(), crate);
            }
        }
        cratesFolder = new File(AquaticCrates.instance().getDataFolder(),"multicrates/");
        cratesFolder.mkdirs();
        for (File file : cratesFolder.listFiles()) {
            MultiCrate crate = new MultiCrateConfig(AquaticCrates.instance(),file).loadCrate();
            if (crate != null) {
                multiCrates.put(crate.getIdentifier(), crate);
            }
        }

        if (crateData.getConfiguration().contains("crates")) {
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
        if (crateData.getConfiguration().contains("multi-crates")) {
            for (String s : crateData.getConfiguration().getStringList("multi-crates")) {
                String[] strs = s.split("\\|");
                MultiCrate crate = getMultiCrate(strs[0]);
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
                spawnMultiCrate(new Location(w,x,y,z,yaw,0),crate);
            }
        }
    }

    public void unloadCrates() {
        for (PlacedCrate pc : locations.values()) {
            pc.destroy();
        }
        for (PlacedMultiCrate pc : multiLocations.values()) {
            pc.destroy();
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
                strs = new ArrayList<>();
                for (Map.Entry<Location, PlacedMultiCrate> entry : multiLocations.entrySet()) {
                    PlacedMultiCrate pc = entry.getValue();
                    Location l = pc.getLocation();

                    strs.add(pc.getMultiCrate().getIdentifier()+"|"+l.getWorld().getName()+"|"+l.getX()+"|"+l.getY()+"|"+l.getZ()+"|"+l.getYaw());
                }
                crateData.getConfiguration().set("multi-crates",strs);
                crateData.save();
            }
        }.runTaskAsynchronously(AquaticCrates.instance());
    }

    public boolean isInAnimation(Player p) {
        for (Crate c : crates.values()) {
            if (c.getAnimationManager().get().isInAnimation(p)) {
                return true;
            }
        }
        return false;
    }

    public boolean skipAnimation(Player p) {
        for (Crate c : crates.values()) {
            if (c.getAnimationManager().get().skipAnimation(p)) return true;
        }
        return false;
    }

    public boolean forceSkipAnimation(Player p) {
        for (Crate c : crates.values()) {
            if (c.getAnimationManager().get().forceSkipAnimation(p)) return true;
        }
        return false;
    }

    public Map<Location, PlacedCrate> getLocations() {
        return locations;
    }

    public Map<String, Crate> getCrates() {
        return crates;
    }

    public Map<Location, PlacedMultiCrate> getMultiLocations() {
        return multiLocations;
    }

    public Map<String, MultiCrate> getMultiCrates() {
        return multiCrates;
    }
}
