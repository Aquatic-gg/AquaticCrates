package cz.larkyy.aquaticcrates.animation;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.animation.showcase.ItemRewardShowcase;
import cz.larkyy.aquaticcrates.animation.showcase.ModelRewardShowcase;
import cz.larkyy.aquaticcrates.animation.showcase.RewardShowcase;
import cz.larkyy.aquaticcrates.crate.reward.Reward;
import cz.larkyy.aquaticcrates.hologram.impl.aquatic.AHologram;
import cz.larkyy.aquaticcrates.model.Model;
import cz.larkyy.aquaticcrates.utils.RewardUtils;
import gg.aquatic.aquaticseries.lib.audience.GlobalAudience;
import gg.aquatic.aquaticseries.lib.audience.WhitelistAudience;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
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

    private AHologram hologram;
    private Reward cachedReward;
    private final Vector offset;
    private RewardShowcase rewardShowcase;
    private final boolean easeOut;

    public static final NamespacedKey REWARD_ITEM_KEY = new NamespacedKey(AquaticCrates.instance(), "AQUATICCRATES_REWARD_ITEM");
    private final Player p;

    public RewardItem(
            Player p,
            Animation animation,
            int rumblingLength,
            int rumblingPeriod,
            int aliveLength,
            Vector vector,
            boolean gravity,
            Vector offset, boolean easeOut
    ) {
        this.p = p;
        this.rumblingLength = rumblingLength;
        this.rumblingPeriod = rumblingPeriod;
        this.aliveLength = aliveLength;
        this.animation = animation;
        this.vector = vector;
        this.gravity = gravity;
        this.easeOut = easeOut;
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
        var settings = reward.getHologram();
        Location loc = rewardShowcase.getLocation().clone().add(settings.getOffset());
        Bukkit.broadcastMessage("Spawning hologram with "+settings.getLines().size()+" lines");
        hologram = new AHologram(loc, settings.getLines());
        if (p == null) {
            hologram.spawn(
                    new GlobalAudience(),
                    list -> {
                        list.replaceAll(s ->
                                s.replace("%reward-name%", reward.getItem().getItem().getItemMeta().getDisplayName()));
                    });
        } else {
            hologram.spawn(
                    new WhitelistAudience(new ArrayList<>() {{
                        add(p.getUniqueId());
                    }}),
                    list -> {
                        list.replaceAll(s ->
                                s.replace("%reward-name%", reward.getItem().getItem().getItemMeta().getDisplayName()));
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

        var settings = reward.getHologram();
        Location loc = rewardShowcase.getLocation().clone().add(settings.getOffset());
        hologram.move(loc);
        hologram.setLines(settings.getLines());
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

        if (cachedReward == null) {
            return;
        }
        var settings = cachedReward.getHologram();
        hologramRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                hologram.move(rewardShowcase.getLocation().clone().add(settings.getOffset()));
            }
        };
        hologramRunnable.runTaskTimer(AquaticCrates.instance(), 0, 1);
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

                var update = false;
                if (!easeOut) {
                    if (tick % rumblingPeriod == 0) {
                        update = true;
                    }
                } else if (shouldPerformAction(tick, rumblingPeriod, rumblingLength)) {
                    update = true;
                }

                if (update) {
                    var rewards = RewardUtils
                            .getPossibleRewards(animation.getPlayer(), animation.getAnimationManager().getCrate().getRewards());

                    Reward r = (Reward) RewardUtils.getRandomReward(
                            rewards,
                            cachedReward
                    );
                    updateItem(r);
                }

                tick++;
            }
        };
        rumbleRunnable.runTaskTimer(AquaticCrates.instance(), 0, 1);
    }

    private boolean shouldPerformAction(int tick, int period, int duration) {
        int thresholdTick = (int) (duration * 0.5);

        if (tick <= thresholdTick) {
            // Perform action at regular intervals before reaching 70% of the duration
            return tick % period == 0;
        } else {
            // Calculate easing based on remaining ticks
            int ticksSinceThreshold = tick - thresholdTick;
            int ticksRemaining = duration - tick;

            // Easing interval increases as we get closer to the end (non-linear easing example)
            double easingRatio = (double) ticksRemaining / (duration - thresholdTick);
            int easingInterval = (int) (period * (1 + (1 - easingRatio) * 4)); // Adjust the multiplier as needed

            // Ensure that the final tick always triggers the action
            return ticksSinceThreshold % easingInterval == 0 || tick == duration;
        }

    }

    private void updateItem(Reward reward) {
        if (rewardShowcase == null) {
            spawnItem(reward);

        }

        var loc = rewardShowcase.getLocation().clone();
        if (rewardShowcase instanceof ItemRewardShowcase itemRewardShowcase) {
            if (reward.getModel() != null) {
                String model = PlaceholderAPI.setPlaceholders(p, reward.getModel());
                rewardShowcase.destroy();
                rewardShowcase = null;
                var l = loc.clone();
                l.setYaw(reward.getModelYaw());
                rewardShowcase = new ModelRewardShowcase(Model.create(model, l, p, p));
                //spawnItem(reward);
            } else {
                itemRewardShowcase.getItem().setItemStack(reward.getItem().getItem());
            }
        } else if (rewardShowcase instanceof ModelRewardShowcase modelRewardShowcase) {
            rewardShowcase.destroy();
            if (reward.getModel() != null) {
                rewardShowcase = null;
                {
                    var item = loc.getWorld().dropItem(loc, reward.getItem().getItem());
                    item.setItemStack(reward.getItem().getItem().clone());
                    item.setPickupDelay(Integer.MAX_VALUE);
                    item.setGravity(gravity);
                    //item.setVelocity(vector);
                    var pdc = item.getPersistentDataContainer();
                    pdc.set(REWARD_ITEM_KEY, PersistentDataType.INTEGER, 1);

                    if (p != null) {
                        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                        players.remove(p);
                        var audience = new WhitelistAudience(new ArrayList<>());
                        for (Player player : players) {
                            audience.add(player);
                        }
                        AquaticCrates.getNmsHandler().despawnEntity(List.of(item.getEntityId()), audience);
                    }
                    rewardShowcase = new ItemRewardShowcase(item);
                }
            } else {
                var l = loc.clone();
                l.setYaw(reward.getModelYaw());
                String model = PlaceholderAPI.setPlaceholders(p, reward.getModel());
                rewardShowcase = new ModelRewardShowcase(Model.create(model, l, p, p));
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
        aliveRunnable.runTaskLater(AquaticCrates.instance(), aliveLength);
    }

    private void spawnItem(Reward reward) {
        Location location = animation.getModel().getLocation().clone().add(0, 1, 0).add(offset);
        if (location.getChunk().isLoaded()) {
            location.getChunk().load();
        }

        if (reward.getModel() != null) {
            var l = location.clone();
            l.setYaw(reward.getModelYaw());
            String model = PlaceholderAPI.setPlaceholders(p, reward.getModel());
            rewardShowcase = new ModelRewardShowcase(Model.create(model, l, p, p));
        } else {
            var item = location.getWorld().dropItem(location, reward.getItem().getItem());
            item.setItemStack(reward.getItem().getItem().clone());
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setGravity(gravity);
            item.setVelocity(vector);
            var pdc = item.getPersistentDataContainer();
            pdc.set(REWARD_ITEM_KEY, PersistentDataType.INTEGER, 1);

            if (p != null) {
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                players.remove(p);
                var audience = new WhitelistAudience(new ArrayList<>());
                for (Player player : players) {
                    audience.add(player);
                }
                AquaticCrates.getNmsHandler().despawnEntity(Arrays.asList(item.getEntityId()), audience);
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

    public AHologram getHologram() {
        return hologram;
    }
}
