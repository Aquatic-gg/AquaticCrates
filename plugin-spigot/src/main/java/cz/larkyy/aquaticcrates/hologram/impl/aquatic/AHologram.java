package cz.larkyy.aquaticcrates.hologram.impl.aquatic;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.hologram.Hologram;
import gg.aquatic.aquaticseries.lib.audience.AquaticAudience;
import gg.aquatic.aquaticseries.lib.audience.GlobalAudience;
import gg.aquatic.aquaticseries.lib.betterhologram.AquaticHologram;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AHologram extends Hologram {

    private ArrayList<AquaticHologram.Line> lines;
    public AHologram(Location location, List<AquaticHologram.Line> lines) {
        super(location);
        this.lines = new ArrayList<>(lines);
    }

    private AquaticAudience audience = new GlobalAudience();

    private AquaticHologram hologram = null;

    public void setLines(List<AquaticHologram.Line> lines) {
        despawn();
        this.lines = new ArrayList<>(lines);
        createHologram();
        hologram.update();
    }

    private void createHologram() {
        var lines = new ArrayList<AquaticHologram.Line>();
        for (AquaticHologram.Line line : this.lines) {
            lines.add(line.clone());
        }
        hologram = new AquaticHologram(
                player -> audience.canBeApplied(player),
                null,
                lines,
                AquaticHologram.Anchor.MIDDLE,
                getLocation(),
                50.0
        );
        AquaticCrates.getHologramHandler().addHologram(hologram);
    }

    @Override
    public void move(Location location) {
        setLocation(location);
        if (hologram == null) return;
        hologram.move(location);
    }

    @Override
    public void despawn() {
        if (hologram == null) return;
        AquaticCrates.getHologramHandler().removeHologram(hologram);
        hologram.despawn();
        hologram = null;
    }

    @Override
    public void spawn(AquaticAudience audience, Consumer<List<String>> consumer) {
        despawn();
        this.audience = audience;
        createHologram();
        hologram.update();
    }

    @Override
    public void update(Consumer<List<String>> consumer) {
    }
}
