package cz.larkyy.aquaticcratestesting.animation.impl;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.animation.Animation;
import cz.larkyy.aquaticcratestesting.animation.AnimationEmote;
import cz.larkyy.aquaticcratestesting.animation.AnimationManager;
import cz.larkyy.aquaticcratestesting.animation.RewardItem;
import cz.larkyy.aquaticcratestesting.api.events.ClaimRewardEvent;
import cz.larkyy.aquaticcratestesting.camera.Camera;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import cz.larkyy.aquaticcratestesting.model.Model;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class CinematicAnimation extends Animation {

    private final Model model;
    private final Camera camera;
    private int i;
    private BukkitRunnable runnable;
    private RewardItem rewardItem = null;
    private final ItemStack helmet;
    private final AnimationEmote emote;

    public CinematicAnimation(AnimationManager animationManager, Player player, AtomicReference<Reward> reward, AnimationEmote emote, Consumer<Animation> callback) {
        super(animationManager, player, reward, callback);

        helmet = player.getInventory().getHelmet();
        this.emote = emote;
        runnable = new BukkitRunnable() {
            @Override
            public void run() {

            }
        };
        runnable.runTask(AquaticCratesTesting.instance());

        if (getAnimationManager().getModelLocation() == null || getAnimationManager().getCameraLocation() == null) {
            model = null;
            camera = null;
            reroll();
        } else {
            model = spawnModel();
            camera = spawnCamera();
            begin();
        }
    }

    @Override
    public void begin() {
        getPlayer().getPersistentDataContainer().set(KEY, PersistentDataType.INTEGER,1);

        var title = getAnimationManager().getPreOpenTitle();
        if (title != null) {
            title.show(getPlayer());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                getPlayer().setInvisible(true);
                camera.attachPlayer(() -> {
                    model.show(getPlayer());
                    start();
                });
            }
        }.runTaskLater(AquaticCratesTesting.instance(), getAnimationManager().getStartDelay());
    }

    @Override
    public void start() {
        setStarted(true);
        emote.play(getPlayer());
        if (getAnimationManager().setPumpkinHelmet()) {
            getPlayer().getInventory().setHelmet(new ItemStack(Material.CARVED_PUMPKIN));
        }
        if (rewardItem != null) {
            rewardItem.despawn();
            rewardItem = null;
        }
        if (camera != null) {
            camera.teleport(getAnimationManager().getCameraLocation().clone().add(0,1.8,0));
        }

        if (getAnimationManager().getModelLocation() == null || getAnimationManager().getCameraLocation() == null) {
            reroll();
            return;
        }
        getAnimationManager().showTitle(getAnimationManager().getOpeningTitle(),getPlayer());
        model.playAnimation("open");
        i = 0;
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
        }
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                getAnimationManager().playTask(i,CinematicAnimation.this);
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
            runnable = null;
        } else if (runnable == null) {
            return;
        }
        getAnimationManager().hideTitle(getPlayer());
        super.reroll();
    }

    @Override
    public void end() {
        emote.despawn(getPlayer());
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
            runnable = null;
        }
        if (rewardItem != null) {
            rewardItem.despawn();
            rewardItem = null;
        }
        getAnimationManager().hideTitle(getPlayer());
        if (model != null) {
            model.hide(getPlayer());
            model.remove();
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
    public void spawnReward(int rumblingLength, int rumblingPeriod, int aliveLength, Vector vector, boolean gravity, Vector offset) {
        if (rewardItem != null) {
            rewardItem.despawn();
        }
        rewardItem = new RewardItem(getPlayer(),this,rumblingLength, rumblingPeriod,aliveLength,vector,gravity,offset);
        rewardItem.spawn();
    }

    private Model spawnModel() {
        String modelNamespace = getAnimationManager().getCrate().getModel();
        return Model.create(modelNamespace,getAnimationManager().getModelLocation(),getPlayer());
    }

    private Camera spawnCamera() {
        return new Camera(getAnimationManager().getCameraLocation(),getPlayer());
    }

    public void moveCamera(Vector offset, int duration, float yawOffset, float pitchOffset) {
        camera.setMovement(offset,duration,yawOffset,pitchOffset);
    }

    public void teleportCamera(Vector offset, float yaw, float pitch) {
        Location location = camera.location().clone().add(offset);
        location.setYaw(yaw);
        location.setPitch(pitch);

        camera.teleport(location);
    }

    @Override
    public Model getModel() {
        return model;
    }
}
