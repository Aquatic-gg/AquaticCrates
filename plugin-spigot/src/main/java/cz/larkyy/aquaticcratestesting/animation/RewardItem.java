package cz.larkyy.aquaticcratestesting.animation;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import cz.larkyy.aquaticcratestesting.hologram.Hologram;
import cz.larkyy.aquaticcratestesting.hologram.impl.AquaticHologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RewardItem {
    private final Animation animation;

    private BukkitRunnable rumbleRunnable;
    private BukkitRunnable aliveRunnable;
    private BukkitRunnable hologramRunnable;
    private final Vector vector;
    private final boolean gravity;

    private final int rumblingLength;
    private final int rumblingPeriod;
    private final int aliveLength;

    private Hologram hologram;
    private Reward cachedReward;
    private Item item;

    private final Player p;

    public RewardItem(
            Player p,
            Animation animation,
            int rumblingLength,
            int rumblingPeriod,
            int aliveLength,
            Vector vector,
            boolean gravity
    ) {
        this.p = p;
        this.rumblingLength = rumblingLength;
        this.rumblingPeriod = rumblingPeriod;
        this.aliveLength = aliveLength;
        this.animation = animation;
        this.vector = vector;
        this.gravity = gravity;
        this.cachedReward = null;
    }

    public void spawn() {
        if (item != null) {
            return;
        }

        startRumbling();
        startAliveCountdown();
        startHologramMoving();
    }

    private void spawnHologram(Reward reward) {
        despawnHologram();

        cachedReward = reward;
        Location loc = item.getLocation().clone().add(0,reward.getHologramYOffset(),0);
        hologram = new AquaticHologram(loc, reward.getHologram());
        if (p == null) {
            hologram.spawn(new ArrayList<>(Bukkit.getOnlinePlayers()));
        } else {
            hologram.spawn(Arrays.asList(p));
        }
    }

    private void updateHologram(Reward reward) {
        if (hologram == null) {
            spawnHologram(reward);
            return;
        }
        if (cachedReward == reward) {
            return;
        }
        cachedReward = reward;

        Location loc = item.getLocation().clone().add(0,reward.getHologramYOffset(),0);
        hologram.setLocation(loc);
        hologram.setLines(reward.getHologram());
        hologram.update();
        /*
        if (p == null) {
            hologram.spawn(new ArrayList<>(Bukkit.getOnlinePlayers()));
        } else {
            hologram.spawn(Arrays.asList(p));
        }

         */
    }

    private void despawnHologram() {
        if (hologram != null) {
            hologram.despawn();
            hologram = null;
        }
    }

    private void startHologramMoving() {
        hologramRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                hologram.move(item.getLocation().clone().add(0,cachedReward.getHologramYOffset(),0));
            }
        };
        hologramRunnable.runTaskTimer(AquaticCratesTesting.instance(),0,1);
    }

    private void startRumbling() {
        rumbleRunnable = new BukkitRunnable() {
            int tick = 0;
            @Override
            public void run() {
                if (tick >= rumblingLength) {
                    updateItem(animation.getReward().get());
                    cancel();
                    return;
                }
                updateItem(animation.getAnimationManager().getCrate().getRandomReward(animation.getPlayer()));
                tick+=rumblingPeriod;
            }
        };
        rumbleRunnable.runTaskTimer(AquaticCratesTesting.instance(),0,rumblingPeriod);
    }

    private void updateItem(Reward reward) {
        if (item == null) {
            spawnItem(reward);
        }
        item.setItemStack(reward.getItem().getItem());
        updateHologram(reward);
    }

    private void startAliveCountdown() {
        aliveRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                despawn();
            }
        };
        aliveRunnable.runTaskLater(AquaticCratesTesting.instance(),aliveLength);
    }

    private void spawnItem(Reward reward) {
        Location location = animation.getModel().getLocation().clone().add(0,1,0);
        item = (Item) location.getWorld().spawnEntity(location,EntityType.DROPPED_ITEM);
        item.setItemStack(reward.getItem().getItem());
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setGravity(gravity);
        item.setVelocity(vector);

        if (p != null) {
            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            players.remove(p);
            AquaticCratesTesting.getNmsHandler().despawnEntity(Arrays.asList(item.getEntityId()),players);
        }
    }

    public void despawn() {
        if (hologramRunnable != null && !hologramRunnable.isCancelled()) {
            hologramRunnable.cancel();
        }

        if (aliveRunnable != null && !aliveRunnable.isCancelled()) {
            aliveRunnable.cancel();
        }
        if (rumbleRunnable != null && !rumbleRunnable.isCancelled()) {
            rumbleRunnable.cancel();
        }

        despawnHologram();

        if (item == null) {
            return;
        }
        item.remove();
        item = null;
    }

}
