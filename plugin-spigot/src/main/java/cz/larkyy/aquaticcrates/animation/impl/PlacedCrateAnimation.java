package cz.larkyy.aquaticcrates.animation.impl;

import com.ticxo.modelengine.api.model.bone.BoneBehaviorTypes;
import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.AnimationManager;
import cz.larkyy.aquaticcrates.animation.RewardItem;
import cz.larkyy.aquaticcrates.crate.PlacedCrate;
import cz.larkyy.aquaticcrates.crate.reward.Reward;
import gg.aquatic.aquaticseries.lib.interactable2.SpawnedInteractable;
import gg.aquatic.aquaticseries.lib.interactable2.impl.meg.ISpawnedMegInteractable;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class PlacedCrateAnimation extends Animation {

    private final PlacedCrate placedCrate;
    private int i;
    private BukkitRunnable runnable;
    private RewardItem rewardItem = null;

    public PlacedCrateAnimation(AnimationManager animationManager, Player player, AtomicReference<Reward> reward, Consumer<Animation> callback, PlacedCrate placedCrate) {
        super(animationManager, player, reward, callback);
        this.placedCrate = placedCrate;
        if (placedCrate == null) {
            reroll();
        } else {
            begin();
        }
    }

    @Override
    public void begin() {

        if (placedCrate.getSpawnedInteractable() instanceof ISpawnedMegInteractable spawnedMegInteractable) {
            /*
            var am = spawnedMegInteractable.getActiveModel();
            if (am != null) {
                var bones = am.getBones().values();
                for (var bone : bones) {
                    bone.getBoneBehavior(BoneBehaviorTypes.PLAYER_LIMB).ifPresent(a -> {
                        a.setTexture(getPlayer());
                    });
                }
            }

             */
        }

        start();
    }

    @Override
    public void start() {
        setStarted(true);
        getAnimationManager().showTitle(getAnimationManager().getOpeningTitle(), getPlayer());
        if (rewardItem != null) {
            rewardItem.despawn();
            rewardItem = null;
        }
        if (placedCrate == null) {
            reroll();
            return;
        }
        String openAnimation;
        if (getReward().get() == null) {
            openAnimation = "open";
        } else {
            openAnimation = getReward().get().getModelAnimation();
            if (openAnimation == null) openAnimation = "open";
        }
        playAnimation(openAnimation);
        i = 0;
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
        }
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                getAnimationManager().playTask(i, PlacedCrateAnimation.this);
                if (getAnimationManager().shouldStopAnimation(i)) {
                    reroll();
                }
                i++;
            }
        };
        runnable.runTaskTimer(AquaticCrates.instance(), 0, 1);
    }

    @Override
    public void reroll() {
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
        }
        getAnimationManager().hideTitle(getPlayer());
        super.reroll();
    }

    @Override
    public void end() {
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
        }
        if (rewardItem != null) {
            rewardItem.despawn();
            rewardItem = null;
        }
        getAnimationManager().hideTitle(getPlayer());
        if (placedCrate != null) {
            stopAnimations();
            playAnimation("idle");
        }
    }

    @Override
    public void spawnReward(int rumblingLength, int rumblingPeriod, int aliveLength, Vector vector, boolean gravity, Vector offset, Boolean easeOut) {
        if (rewardItem != null) {
            rewardItem.despawn();
        }
        rewardItem = new RewardItem(null, this, rumblingLength, rumblingPeriod, aliveLength, vector, gravity, offset, easeOut);
        rewardItem.spawn();
    }

    @Override
    public SpawnedInteractable<?> getModel() {
        return placedCrate.getSpawnedInteractable();
    }
}
