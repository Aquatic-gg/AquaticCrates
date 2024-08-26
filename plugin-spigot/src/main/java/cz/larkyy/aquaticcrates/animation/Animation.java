package cz.larkyy.aquaticcrates.animation;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.crate.reward.Reward;
import gg.aquatic.aquaticseries.lib.interactable2.SpawnedInteractable;
import gg.aquatic.aquaticseries.lib.interactable2.impl.meg.ISpawnedMegInteractable;
import gg.aquatic.aquaticseries.lib.interactable2.impl.meg.SpawnedMegInteractable;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public abstract class Animation {

    public static final NamespacedKey KEY = new NamespacedKey(AquaticCrates.instance(),"inAnimation");

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
        getAnimationManager().addAnimation(getPlayer(),this);
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
            boolean gravity,
            Vector offset,
            Boolean easeOut
    );
    public abstract SpawnedInteractable<?> getModel();
    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public void playAnimation(String animation) {
        if (getModel() instanceof ISpawnedMegInteractable megInteractable) {
            megInteractable.getActiveModel().getAnimationHandler().playAnimation(animation, 0d, 0d, 1.0, true);
        }
    }
}
