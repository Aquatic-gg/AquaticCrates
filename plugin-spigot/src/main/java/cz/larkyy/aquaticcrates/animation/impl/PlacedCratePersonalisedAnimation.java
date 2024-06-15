package cz.larkyy.aquaticcrates.animation.impl;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.AnimationManager;
import cz.larkyy.aquaticcrates.animation.RewardItem;
import cz.larkyy.aquaticcrates.crate.PlacedCrate;
import cz.larkyy.aquaticcrates.crate.reward.Reward;
import cz.larkyy.aquaticcrates.model.Model;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class PlacedCratePersonalisedAnimation extends Animation {

    private final Model model;
    private final PlacedCrate placedCrate;
    private int i;
    private BukkitRunnable runnable;
    private RewardItem rewardItem = null;

    public PlacedCratePersonalisedAnimation(AnimationManager animationManager, Player player, AtomicReference<Reward> reward, Consumer<Animation> callback, PlacedCrate placedCrate) {
        super(animationManager, player, reward, callback);
        this.placedCrate = placedCrate;
        if (placedCrate == null) {
            this.model = null;
            reroll();
        } else {
            placedCrate.getModel().hide(player);
            this.model = Model.create(placedCrate.getCrate().getModel(),placedCrate.getLocation(),player,player);
            begin();
        }
    }

    @Override
    public void begin() {
        getPlayer().getPersistentDataContainer().set(KEY, PersistentDataType.INTEGER,1);
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
        if (model == null) {
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
        model.playAnimation(openAnimation);
        i = 0;
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
        }
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                getAnimationManager().playTask(i,PlacedCratePersonalisedAnimation.this);
                if (getAnimationManager().shouldStopAnimation(i)) {
                    reroll();
                }
                i++;
            }
        };
        runnable.runTaskTimer(AquaticCrates.instance(),0,1);
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
        if (model != null) {
            model.remove();
        }
        if (placedCrate != null) {
            placedCrate.getModel().show(getPlayer());
        }
        getPlayer().getPersistentDataContainer().remove(KEY);
    }

    @Override
    public void spawnReward(int rumblingLength, int rumblingPeriod, int aliveLength, Vector vector, boolean gravity, Vector offset) {
        if (rewardItem != null) {
            rewardItem.despawn();
        }
        rewardItem = new RewardItem(getPlayer(),this,rumblingLength, rumblingPeriod,aliveLength,vector,gravity,offset);
        rewardItem.spawn();
    }

    @Override
    public Model getModel() {
        return model;
    }
}
