package cz.larkyy.aquaticcrates.hologram.impl;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.hologram.Hologram;
import cz.larkyy.aquaticcrates.utils.Utils;
import gg.aquatic.aquaticseries.lib.StringExtKt;
import gg.aquatic.aquaticseries.lib.audience.WhitelistAudience;
import gg.aquatic.aquaticseries.lib.nms.NMSAdapter;
import gg.aquatic.aquaticseries.lib.util.AbstractAudience;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AquaticHologram extends Hologram {

    private final List<Integer> ids;
    private final List<Player> visitors;
    private boolean hidden;

    public AquaticHologram(Location location, List<String> lines) {
        super(location, lines);
        this.ids = new ArrayList<>();
        this.visitors = new ArrayList<>();
        hidden = false;
    }

    @Override
    public void teleport(Location location) {
        setLocation(location);
        new BukkitRunnable() {
            @Override
            public void run() {
                var audience = new WhitelistAudience(new ArrayList<>());
                for (Player visitor : visitors) {
                    audience.add(visitor);
                }
                int i = getLines().size();
                for (Integer id : ids) {
                    nmsHandler().teleportEntity(id, location.clone().add(0, 0.25 * i, 0), audience);
                    i--;
                }
            }
        }.runTask(AquaticCrates.instance());
    }

    @Override
    public void move(Location location) {
        if (Utils.isVectorSame(getLocation().toVector(), location.toVector())) {
            return;
        }
        setLocation(location);
        new BukkitRunnable() {
            @Override
            public void run() {
                var audience = new WhitelistAudience(new ArrayList<>());
                for (Player visitor : visitors) {
                    audience.add(visitor);
                }
                int i = getLines().size();
                for (Integer id : ids) {
                    nmsHandler().moveEntity(id, location.clone().add(0, 0.25 * i, 0), audience);
                    i--;
                }
            }
        }.runTask(AquaticCrates.instance());
    }

    @Override
    public void despawn() {
        var audience = new WhitelistAudience(new ArrayList<>());
        for (Player visitor : visitors) {
            audience.add(visitor);
        }
        nmsHandler().despawnEntity(ids, audience);
        visitors.clear();
        ids.clear();
    }

    @Override
    public void spawn(List<Player> visitors, Consumer<List<String>> consumer) {

        var filtered = visitors.stream().filter(player -> player.getLocation().getWorld().getName().equals(getLocation().getWorld().getName())).toList();

        if (hidden) {
            addAllVisitors(filtered);
            return;
        }

        List<String> lines = new ArrayList<>(getLines());
        consumer.accept(lines);

        despawn();
        addAllVisitors(filtered);

        int i = lines.size();
        for (String line : lines) {
            ids.add(spawnLine(getLocation().clone().add(0, 0.25 * i, 0), line));
            i--;
        }
    }

    private void addAllVisitors(List<Player> players) {
        for (Player p : players) {
            if (visitors.contains(p)) {
                continue;
            }
            visitors.add(p);
        }
    }

    @Override
    public void hide() {
        hidden = true;
        var audience = new WhitelistAudience(new ArrayList<>());
        for (Player visitor : visitors) {
            audience.add(visitor);
        }
        nmsHandler().despawnEntity(ids, audience);
        ids.clear();
    }

    @Override
    public void show() {
        if (!hidden) {
            return;
        }
        hidden = false;
        spawn(visitors, list -> {
        });
    }

    @Override
    public void update(Consumer<List<String>> consumer) {
        var audience = new WhitelistAudience(new ArrayList<>());
        for (Player visitor : visitors) {
            audience.add(visitor);
        }
        if (getLines() == null || getLines().isEmpty()) {
            nmsHandler().despawnEntity(ids, audience);
            ids.clear();
            return;
        }
        List<String> lines = new ArrayList<>(getLines());
        consumer.accept(lines);

        if (ids.size() > lines.size()) {
            int remove = ids.size() - lines.size();
            List<Integer> idsToRemove = new ArrayList<>();
            for (int i = 0; i < remove; i++) {
                idsToRemove.add(ids.get(ids.size() - 1 - i));
            }
            nmsHandler().despawnEntity(idsToRemove, audience);
            ids.removeAll(idsToRemove);
        }

        int lineNumber = 0;
        for (String line : lines) {
            final String formattedLine = visitors.isEmpty() ? line : PlaceholderAPI.setPlaceholders(visitors.stream().findFirst().get(), line);

            Location l2 = getLocation().clone().add(0, 0.25 * (lines.size() - lineNumber), 0);
            if (lineNumber < ids.size()) {
                int id = ids.get(lineNumber);
                Entity entity = nmsHandler().getEntity(id);
                Location l1 = entity.getLocation();

                if (!l1.getWorld().equals(l2.getWorld()) || !Utils.isVectorSame(l1.toVector(), l2.toVector())
                ) {
                    nmsHandler().teleportEntity(id, l2, audience);
                }
                nmsHandler().updateEntity(id, e -> {
                    StringExtKt.toAquatic(formattedLine).setEntityName(e);
                }, audience);
            } else {
                ids.add(spawnLine(l2, formattedLine));
            }
            lineNumber++;
        }
    }

    private int spawnLine(Location location, String text) {
        final String formattedLine = visitors.isEmpty() ? text : PlaceholderAPI.setPlaceholders(visitors.get(0), text);

        var audience = new WhitelistAudience(new ArrayList<>());
        for (Player visitor : visitors) {
            audience.add(visitor);
        }

        return nmsHandler().spawnEntity(
                location,
                "area_effect_cloud",
                audience,
                e -> {
                    AreaEffectCloud aec = (AreaEffectCloud) e;
                    aec.setRadius(0f);
                    StringExtKt.toAquatic(formattedLine).setEntityName(e);
                    e.setCustomNameVisible(true);
                    e.setGravity(false);
                    e.setPersistent(false);
                }

        );
    }

    private NMSAdapter nmsHandler() {
        return AquaticCrates.getNmsHandler();
    }
}
