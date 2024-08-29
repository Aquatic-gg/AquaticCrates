package cz.larkyy.aquaticcrates.animation.impl;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.AnimationManager;
import cz.larkyy.aquaticcrates.animation.RewardItem;
import cz.larkyy.aquaticcrates.camera.Camera;
import cz.larkyy.aquaticcrates.crate.reward.Reward;
import gg.aquatic.aquaticseries.lib.audience.WhitelistAudience;
import gg.aquatic.aquaticseries.lib.interactable2.AbstractSpawnedPacketInteractable;
import gg.aquatic.aquaticseries.lib.interactable2.SpawnedInteractable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class CinematicAnimation extends Animation {

    private final AbstractSpawnedPacketInteractable<?> spawnedInteractable;
    private final Camera camera;
    private int i;
    private BukkitRunnable runnable;
    private RewardItem rewardItem = null;
    private final ItemStack helmet;

    public CinematicAnimation(AnimationManager animationManager, Player player, AtomicReference<Reward> reward, Consumer<Animation> callback) {
        super(animationManager, player, reward, callback);

        helmet = player.getInventory().getHelmet();
        runnable = new BukkitRunnable() {
            @Override
            public void run() {

            }
        };
        runnable.runTask(AquaticCrates.instance());

        if (getAnimationManager().getModelLocation() == null || getAnimationManager().getCameraLocation() == null) {
            spawnedInteractable = null;
            camera = null;
            reroll();
        } else {
            spawnedInteractable = spawnModel();
            camera = spawnCamera();
            begin();
        }
    }

    @Override
    public void begin() {
        getPlayer().getPersistentDataContainer().set(KEY, PersistentDataType.INTEGER, 1);

        var title = getAnimationManager().getPreOpenTitle();
        if (title != null) {
            title.show(getPlayer());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                getPlayer().setInvisible(true);
                camera.attachPlayer(() -> {

                    spawnedInteractable.show(getPlayer());
                    start();
                });
            }
        }.runTaskLater(AquaticCrates.instance(), getAnimationManager().getStartDelay());
    }

    @Override
    public void start() {
        setStarted(true);
        if (getAnimationManager().setPumpkinHelmet()) {
            getPlayer().getInventory().setHelmet(new ItemStack(Material.CARVED_PUMPKIN));
        }
        if (rewardItem != null) {
            rewardItem.despawn();
            rewardItem = null;
        }
        if (camera != null) {
            camera.teleport(getAnimationManager().getCameraLocation().clone().add(0, 1.8, 0));
        }

        if (getAnimationManager().getModelLocation() == null || getAnimationManager().getCameraLocation() == null) {
            reroll();
            return;
        }

        getAnimationManager().showTitle(getAnimationManager().getOpeningTitle(), getPlayer());
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
                getAnimationManager().playTask(i, CinematicAnimation.this);
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
            runnable = null;
        } else if (runnable == null) {
            return;
        }
        getAnimationManager().hideTitle(getPlayer());
        super.reroll();
    }

    @Override
    public void end() {
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
            runnable = null;
        }
        if (rewardItem != null) {
            rewardItem.despawn();
            rewardItem = null;
        }
        getAnimationManager().hideTitle(getPlayer());
        if (spawnedInteractable != null) {
            spawnedInteractable.hide(getPlayer());
            spawnedInteractable.despawn();
        }
        //Bukkit.broadcastMessage("Detaching & removing");
        if (camera != null) {
            camera.despawn();
        }
        if (getAnimationManager().setPumpkinHelmet()) {
            getPlayer().getInventory().setHelmet(helmet);
        }
        getPlayer().setInvisible(false);
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

    private AbstractSpawnedPacketInteractable<?> spawnModel() {
        var spawned = getAnimationManager().getCrate().getInteractable().spawnPacket(
                getAnimationManager().getModelLocation(),
                new WhitelistAudience(new ArrayList<>() { { add(getPlayer().getUniqueId()); } }),
                false
        );
        spawned.show(getPlayer());
        return spawned;
    }

    private Camera spawnCamera() {
        return new Camera(getAnimationManager().getCameraLocation(), getPlayer());
    }

    public void moveCamera(Vector offset, int duration, float yawOffset, float pitchOffset) {
        camera.setMovement(offset, duration, yawOffset, pitchOffset);
    }

    public void teleportCamera(Vector offset, float yaw, float pitch) {
        Location location = camera.location().clone().add(offset);
        location.setYaw(yaw);
        location.setPitch(pitch);

        camera.teleport(location);
    }

    @Override
    public SpawnedInteractable<?> getModel() {
        return spawnedInteractable;
    }
}
