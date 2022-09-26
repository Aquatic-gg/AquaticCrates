package cz.larkyy.aquaticcratestesting.hologram.impl;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.hologram.Hologram;
import cz.larkyy.aquaticcratestesting.nms.NMSHandler;
import cz.larkyy.aquaticcratestesting.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.colorutils.Colors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AquaticHologram extends Hologram {

    private final List<Integer> ids;
    private List<Player> visitors;
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
                int i = getLines().size();
                for (Integer id : ids) {
                    nmsHandler().teleportEntity(id,location.clone().add(0,0.25*i,0));
                    i--;
                }
            }
        }.runTask(AquaticCratesTesting.instance());
    }

    @Override
    public void move(Location location) {
        if (Utils.isVectorSame(getLocation().toVector(),location.toVector())) {
            return;
        }
        setLocation(location);
        new BukkitRunnable() {
            @Override
            public void run() {
                int i = getLines().size();
                for (Integer id : ids) {
                    nmsHandler().moveEntity(id,location.clone().add(0,0.25*i,0));
                    i--;
                }
            }
        }.runTask(AquaticCratesTesting.instance());
    }

    @Override
    public void despawn() {
        nmsHandler().despawnEntity(ids,visitors);
        visitors.clear();
        ids.clear();
    }

    @Override
    public void spawn(List<Player> visitors, Consumer<List<String>> consumer) {

        if (hidden) {
            addAllVisitors(visitors);
            return;
        }

        List<String> lines = new ArrayList<>(getLines());
        consumer.accept(lines);

        despawn();
        addAllVisitors(visitors);

        int i = lines.size();
        for (String line : lines) {
            ids.add(spawnLine(getLocation().clone().add(0,0.25*i,0),line));
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
        nmsHandler().despawnEntity(ids,visitors);
        ids.clear();
    }

    @Override
    public void show() {
        if (!hidden) {
            return;
        }
        hidden = false;
        spawn(visitors, list -> {});
    }

    @Override
    public void update(Consumer<List<String>> consumer) {
        if (getLines() == null || getLines().isEmpty()) {
            nmsHandler().despawnEntity(ids, visitors);
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
            nmsHandler().despawnEntity(idsToRemove, visitors);
            ids.removeAll(idsToRemove);
        }

        int lineNumber = 0;
        for (String line : lines) {
            Location l2 = getLocation().clone().add(0, 0.25 * (lines.size() - lineNumber), 0);
            if (lineNumber < ids.size()) {
                int id = ids.get(lineNumber);
                Entity entity = nmsHandler().getEntity(id);
                Location l1 = entity.getLocation();

                //Bukkit.broadcastMessage("Previous Y: "+l1.getY()+" New Y: "+l2.getY());
                if (!l1.getWorld().equals(l2.getWorld()) || !Utils.isVectorSame(l1.toVector(), l2.toVector())
                ) {
                    nmsHandler().teleportEntity(id, l2);
                }
                nmsHandler().updateEntity(id, e -> {
                    e.setCustomName(Colors.format(line));
                });
            } else {
                ids.add(spawnLine(l2,line));
            }
            lineNumber++;
        }
    }

    private int spawnLine(Location location, String text) {
        return nmsHandler().spawnEntity(
                location,
                e -> {
                    AreaEffectCloud aec = (AreaEffectCloud) e;
                    aec.setRadius(0f);
                    e.setCustomName(Colors.format(text));
                    e.setCustomNameVisible(true);
                    e.setGravity(false);
                    e.setPersistent(false);
                    /*ArmorStand as = (ArmorStand) e;
                    as.setPersistent(false);
                    as.setGravity(false);
                    as.setCollidable(false);
                    as.setInvisible(true);
                    as.setCustomNameVisible(true);
                    as.setCustomName(Colors.format(text));

                     */
                },
                visitors,
                "area_effect_cloud"
                );
    }

    private NMSHandler nmsHandler() {
        return AquaticCratesTesting.getNmsHandler();
    }
}
