package cz.larkyy.aquaticcrates.animation.impl;

import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.AnimationManager;
import cz.larkyy.aquaticcrates.crate.reward.Reward;
import gg.aquatic.aquaticseries.lib.interactable2.SpawnedInteractable;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class InstantAnimation extends Animation {

    public InstantAnimation(AnimationManager animationManager, Player player, AtomicReference<Reward> reward, Consumer<Animation> callback) {
        super(animationManager,player, reward, callback);
        begin();
    }

    public void begin() {
        start();
    }

    public void start() {
        reroll();
    }

    @Override
    public void reroll() {
        getAnimationManager().hideTitle(getPlayer());
        super.reroll();
    }

    public void end() {
        getAnimationManager().hideTitle(getPlayer());
    }

    @Override
    public void spawnReward(int rumblingLength, int rumblingPeriod, int aliveLength, Vector vector, boolean gravity, Vector offset, Boolean easeOut) {

    }

    @Override
    public SpawnedInteractable<?> getModel() {
        return null;
    }
}
