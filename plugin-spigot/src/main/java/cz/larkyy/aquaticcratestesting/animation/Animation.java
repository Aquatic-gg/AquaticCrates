package cz.larkyy.aquaticcratestesting.animation;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import cz.larkyy.aquaticcratestesting.model.Model;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public abstract class Animation {

    public static final NamespacedKey KEY = new NamespacedKey(AquaticCratesTesting.instance(),"inAnimation");

    private final Player player;
    private Consumer<Animation> callback;
    private final AtomicReference<Reward> reward;
    private final AnimationManager animationManager;
    private boolean started;

    public Animation(AnimationManager animationManager, Player player, AtomicReference<Reward> reward, Consumer<Animation> callback) {
        this.animationManager = animationManager;
        this.player = player;
        this.callback = callback;
        this.reward = reward;
        started = false;
    }

    public abstract void begin();

    public abstract void start();

    public void reroll() {
        getCallback().accept(this);
    }

    public abstract void end();

    public Player getPlayer() {
        return player;
    }

    public Consumer<Animation> getCallback() {
        return callback;
    }

    public void setCallback(Consumer<Animation> callback) {
        this.callback = callback;
    }

    public AtomicReference<Reward> getReward() {
        return reward;
    }

    public abstract void spawnReward(
            int rumblingLength,
            int rumblingPeriod,
            int aliveLength,
            Vector vector,
            boolean gravity
    );
    public abstract Model getModel();
    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
