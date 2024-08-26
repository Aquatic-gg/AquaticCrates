package cz.larkyy.aquaticcrates.crate.model;
import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.crate.Crate;
import cz.larkyy.aquaticcrates.crate.CrateBase;
import cz.larkyy.aquaticcrates.model.Model;
import gg.aquatic.aquaticseries.lib.interactable2.SpawnedInteractable;
import gg.aquatic.aquaticseries.lib.interactable2.impl.meg.ISpawnedMegInteractable;
import org.bukkit.scheduler.BukkitRunnable;

public class ModelAnimationHandler {

    private final CrateBase crateBase;
    private boolean cancelled = false;
    private final SpawnedInteractable<?> spawnedInteractable;

    public ModelAnimationHandler(SpawnedInteractable<?> spawnedInteractable, CrateBase crateBase) {
        this.spawnedInteractable = spawnedInteractable;
        this.crateBase = crateBase;

        playNext();
    }

    public void playNext() {
        if (crateBase.getModelSettings().getModelAnimations().getPeriod() < 0) return;
        var animation = crateBase.getModelSettings().getModelAnimations().getRandomAnimation();
        if (animation == null) return;
        var period = crateBase.getModelSettings().getModelAnimations().getPeriod();

        if (crateBase instanceof Crate crate) {
            if (!crate.getAnimationManager().get().isAnyoneOpening()) {
                playAnimation(animation.getAnimationId());
            }
        } else {
            playAnimation(animation.getAnimationId());
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (cancelled) return;
                playNext();
            }
        }.runTaskLater(AquaticCrates.instance(),period+ animation.getAnimationLength());
    }

    public void playAnimation(String animationId) {
        if (spawnedInteractable instanceof ISpawnedMegInteractable megInteractable) {
            megInteractable.getActiveModel().getAnimationHandler().playAnimation(animationId, 0d,0d,1.0,true);
        }
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
