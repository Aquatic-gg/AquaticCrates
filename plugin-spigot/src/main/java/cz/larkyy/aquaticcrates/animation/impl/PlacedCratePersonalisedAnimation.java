package cz.larkyy.aquaticcrates.animation.impl;

import com.ticxo.modelengine.api.model.bone.BoneBehaviorTypes;
import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.AnimationManager;
import cz.larkyy.aquaticcrates.animation.RewardItem;
import cz.larkyy.aquaticcrates.crate.PlacedCrate;
import cz.larkyy.aquaticcrates.crate.reward.Reward;
import gg.aquatic.aquaticseries.lib.audience.WhitelistAudience;
import gg.aquatic.aquaticseries.lib.interactable2.AbstractSpawnedPacketInteractable;
import gg.aquatic.aquaticseries.lib.interactable2.SpawnedInteractable;
import gg.aquatic.aquaticseries.lib.interactable2.impl.meg.ISpawnedMegInteractable;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class PlacedCratePersonalisedAnimation extends Animation {

    private final AbstractSpawnedPacketInteractable<?> spawnedInteractable;
    private final PlacedCrate placedCrate;
    private int i;
    private BukkitRunnable runnable;
    private RewardItem rewardItem = null;

    public PlacedCratePersonalisedAnimation(AnimationManager animationManager, Player player, AtomicReference<Reward> reward, Consumer<Animation> callback, PlacedCrate placedCrate) {
        super(animationManager, player, reward, callback);
        this.placedCrate = placedCrate;
        if (placedCrate == null) {
            this.spawnedInteractable = null;
            reroll();
        } else {
            var placedCrateInteractable = placedCrate.getSpawnedInteractable();
            if (placedCrateInteractable instanceof AbstractSpawnedPacketInteractable<?> abstractSpawnedPacketInteractable) {
                abstractSpawnedPacketInteractable.hide(player);
            } else if (placedCrateInteractable instanceof ISpawnedMegInteractable megInteractable) {
                megInteractable.getDummy().setForceHidden(player,true);
            }
            this.spawnedInteractable = placedCrateInteractable.getBase().spawnPacket(placedCrate.getLocation(), new WhitelistAudience(new ArrayList<>() {{ add(player.getUniqueId()); }}), false, false);
            begin();
        }
    }

    @Override
    public void begin() {
        if (spawnedInteractable instanceof ISpawnedMegInteractable spawnedMegInteractable) {
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
        getPlayer().getPersistentDataContainer().set(KEY, PersistentDataType.INTEGER, 1);
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
        if (spawnedInteractable == null) {
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
                getAnimationManager().playTask(i, PlacedCratePersonalisedAnimation.this);
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
        if (spawnedInteractable != null) {
            spawnedInteractable.despawn();
        }
        if (placedCrate != null) {
            var placedCrateInteractable = placedCrate.getSpawnedInteractable();
            if (placedCrateInteractable instanceof AbstractSpawnedPacketInteractable<?> abstractSpawnedPacketInteractable) {
                abstractSpawnedPacketInteractable.show(getPlayer());
            } else if (placedCrateInteractable instanceof ISpawnedMegInteractable megInteractable) {
                megInteractable.getDummy().setForceHidden(getPlayer(),false);
            }
        }
        getPlayer().getPersistentDataContainer().remove(KEY);
    }

    @Override
    public void spawnReward(int rumblingLength, int rumblingPeriod, int aliveLength, Vector vector, boolean gravity, Vector offset, Boolean easeOut) {
        if (rewardItem != null) {
            rewardItem.despawn();
        }
        rewardItem = new RewardItem(getPlayer(), this, rumblingLength, rumblingPeriod, aliveLength, vector, gravity, offset, easeOut);
        rewardItem.spawn();
    }

    @Override
    public SpawnedInteractable<?> getModel() {
        return spawnedInteractable;
    }
}
