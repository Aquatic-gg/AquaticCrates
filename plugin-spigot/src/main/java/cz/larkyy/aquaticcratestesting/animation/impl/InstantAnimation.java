package cz.larkyy.aquaticcratestesting.animation.impl;

import cz.larkyy.aquaticcratestesting.animation.Animation;
import cz.larkyy.aquaticcratestesting.animation.AnimationManager;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import cz.larkyy.aquaticcratestesting.model.Model;
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
    public void spawnReward(int rumblingLength, int rumblingPeriod, int aliveLength, Vector vector, boolean gravity, Vector offset) {

    }

    @Override
    public Model getModel() {
        return null;
    }
}
