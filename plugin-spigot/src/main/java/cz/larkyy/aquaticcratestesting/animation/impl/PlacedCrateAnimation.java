package cz.larkyy.aquaticcratestesting.animation.impl;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.animation.Animation;
import cz.larkyy.aquaticcratestesting.animation.AnimationManager;
import cz.larkyy.aquaticcratestesting.animation.RewardItem;
import cz.larkyy.aquaticcratestesting.crate.PlacedCrate;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import cz.larkyy.aquaticcratestesting.model.Model;
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
        start();
    }

    @Override
    public void start() {
        setStarted(true);
        getAnimationManager().showTitle(getAnimationManager().getOpeningTitle(),getPlayer());
        if (rewardItem != null) {
            rewardItem.despawn();
            rewardItem = null;
        }
        if (placedCrate == null) {
            reroll();
            return;
        }
        placedCrate.getModel().playAnimation("open");
        i = 0;
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
        }
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                getAnimationManager().playTask(i,PlacedCrateAnimation.this);
                if (getAnimationManager().shouldStopAnimation(i)) {
                    reroll();
                }
                i++;
            }
        };
        runnable.runTaskTimer(AquaticCratesTesting.instance(),0,1);
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
            getModel().playAnimation("idle");
        }
    }

    @Override
    public void spawnReward(int rumblingLength, int rumblingPeriod, int aliveLength, Vector vector, boolean gravity) {
        if (rewardItem != null) {
            rewardItem.despawn();
        }
        rewardItem = new RewardItem(null,this,rumblingLength, rumblingPeriod,aliveLength,vector,gravity);
        rewardItem.spawn();
    }

    @Override
    public Model getModel() {
        return placedCrate.getModel();
    }
}
