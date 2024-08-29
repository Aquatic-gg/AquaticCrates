package cz.larkyy.aquaticcrates.hologram;

import cz.larkyy.aquaticcrates.AquaticCrates;
import gg.aquatic.aquaticseries.lib.betterhologram.AquaticHologram;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class HologramHandler {

    public HologramHandler() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (AquaticHologram aquaticHologram : spawned) {
                    aquaticHologram.update();
                }
            }
        }.runTaskTimer(AquaticCrates.instance(), 0, 1);
    }

    private final List<AquaticHologram> spawned = new ArrayList<>();

    public void addHologram(AquaticHologram hologram) {
        spawned.add(hologram);
    }

    public void removeHologram(AquaticHologram hologram) {
        spawned.remove(hologram);
    }

}
