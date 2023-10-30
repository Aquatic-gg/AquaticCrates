package cz.larkyy.aquaticcratestesting.animation;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.animation.showcase.ItemRewardShowcase;
import cz.larkyy.aquaticcratestesting.animation.showcase.ModelRewardShowcase;
import cz.larkyy.aquaticcratestesting.animation.showcase.RewardShowcase;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import cz.larkyy.aquaticcratestesting.hologram.Hologram;
import cz.larkyy.aquaticcratestesting.hologram.impl.AquaticHologram;
import cz.larkyy.aquaticcratestesting.model.Model;
import cz.larkyy.aquaticcratestesting.utils.RewardUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;
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
    private final Vector offset;
    private RewardShowcase rewardShowcase;

    public static final NamespacedKey REWARD_ITEM_KEY = new NamespacedKey(AquaticCratesTesting.instance(),"AQUATICCRATES_REWARD_ITEM");
    private final Player p;

    public RewardItem(
            Player p,
            Animation animation,
            int rumblingLength,
            int rumblingPeriod,
            int aliveLength,
            Vector vector,
            boolean gravity,
            Vector offset
    ) {
        this.p = p;
        this.rumblingLength = rumblingLength;
        this.rumblingPeriod = rumblingPeriod;
        this.aliveLength = aliveLength;
        this.animation = animation;
        this.vector = vector;
        this.gravity = gravity;
        this.cachedReward = null;
        this.offset = offset;
    }

    public void spawn() {
        if (rewardShowcase != null) {
            return;
        }

        startRumbling();
        startAliveCountdown();
        startHologramMoving();
    }

    private void spawnHologram(Reward reward) {
        despawnHologram();

        cachedReward = reward;
        Location loc = rewardShowcase.getLocation().clone().add(0,reward.getHologramYOffset(),0);
        hologram = new AquaticHologram(loc, reward.getHologram());
        if (p == null) {
            hologram.spawn(
                    new ArrayList<>(Bukkit.getOnlinePlayers()),
                    list -> {
                        list.replaceAll(s ->
                                s.replace("%reward-name%",reward.getItem().getItem().getItemMeta().getDisplayName()));
                    });
        } else {
            hologram.spawn(
                    Arrays.asList(p),
                    list -> {
                        list.replaceAll(s ->
                                s.replace("%reward-name%",reward.getItem().getItem().getItemMeta().getDisplayName()));
                    });
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

        Location loc = rewardShowcase.getLocation().clone().add(0,reward.getHologramYOffset(),0);
        hologram.setLocation(loc);
        hologram.setLines(reward.getHologram());
        hologram.update(list -> {
            list.replaceAll(s ->
                    s.replace("%reward-name%",reward.getItem().getItem().getItemMeta().getDisplayName()));
        });
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
                hologram.move(rewardShowcase.getLocation().clone().add(0,cachedReward.getHologramYOffset(),0));
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
                Reward r = RewardUtils.getRandomReward(
                        animation.getPlayer(),
                        animation.getAnimationManager().getCrate().getRewards(),
                        cachedReward,
                        animation.getAnimationManager().getCrate()
                );
                updateItem(r);
                tick+=rumblingPeriod;
            }
        };
        rumbleRunnable.runTaskTimer(AquaticCratesTesting.instance(),0,rumblingPeriod);
    }

    private void updateItem(Reward reward) {
        if (rewardShowcase == null) {
            spawnItem(reward);
        }

        var loc = rewardShowcase.getLocation().clone();
        if (rewardShowcase instanceof ItemRewardShowcase itemRewardShowcase) {
            if (reward.getModel() != null) {
                String model = PlaceholderAPI.setPlaceholders(p,reward.getModel());
                rewardShowcase.destroy();
                rewardShowcase = null;
                var l = loc.clone();
                l.setYaw(reward.getModelYaw());
                rewardShowcase = new ModelRewardShowcase(Model.create(model,l,p,p));
                //spawnItem(reward);
            } else {
                itemRewardShowcase.getItem().setItemStack(reward.getItem().getItem());
            }
        } else if (rewardShowcase instanceof ModelRewardShowcase modelRewardShowcase) {
            rewardShowcase.destroy();
            if (reward.getModel() != null) {
                rewardShowcase = null;
                {
                    var item = loc.getWorld().dropItem(loc,reward.getItem().getItem());
                    item.setItemStack(reward.getItem().getItem().clone());
                    item.setPickupDelay(Integer.MAX_VALUE);
                    item.setGravity(gravity);
                    //item.setVelocity(vector);
                    var pdc = item.getPersistentDataContainer();
                    pdc.set(REWARD_ITEM_KEY, PersistentDataType.INTEGER, 1);

                    if (p != null) {
                        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                        players.remove(p);
                        AquaticCratesTesting.getNmsHandler().despawnEntity(Arrays.asList(item.getEntityId()),players);
                    }
                    rewardShowcase = new ItemRewardShowcase(item);
                }
            } else {
                var l = loc.clone();
                l.setYaw(reward.getModelYaw());
                String model = PlaceholderAPI.setPlaceholders(p,reward.getModel());
                rewardShowcase = new ModelRewardShowcase(Model.create(model,l,p,p));
            }
        }
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
        Location location = animation.getModel().getLocation().clone().add(0,1,0).add(offset);
        if (location.getChunk().isLoaded()) {
            location.getChunk().load();
        }

        if (reward.getModel() != null) {
            var l = location.clone();
            l.setYaw(reward.getModelYaw());
            String model = PlaceholderAPI.setPlaceholders(p,reward.getModel());
            rewardShowcase = new ModelRewardShowcase(Model.create(model,l,p,p));
        } else {
            var item = location.getWorld().dropItem(location,reward.getItem().getItem());
            item.setItemStack(reward.getItem().getItem().clone());
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setGravity(gravity);
            item.setVelocity(vector);
            var pdc = item.getPersistentDataContainer();
            pdc.set(REWARD_ITEM_KEY, PersistentDataType.INTEGER, 1);

            if (p != null) {
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                players.remove(p);
                AquaticCratesTesting.getNmsHandler().despawnEntity(Arrays.asList(item.getEntityId()),players);
            }
            rewardShowcase = new ItemRewardShowcase(item);
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

        if (rewardShowcase == null) {
            return;
        }
        rewardShowcase.destroy();
        rewardShowcase = null;
    }

}
