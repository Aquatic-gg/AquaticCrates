package cz.larkyy.aquaticcrates.crate.model;
import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.crate.Crate;
import cz.larkyy.aquaticcrates.crate.CrateBase;
import cz.larkyy.aquaticcrates.model.Model;
import org.bukkit.scheduler.BukkitRunnable;

public class ModelAnimationHandler {

    private final CrateBase crateBase;
    private final Model model;
    private boolean cancelled = false;

    public ModelAnimationHandler(Model model, CrateBase crateBase) {
        this.model = model;
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
                model.playAnimation(animation.getAnimationId());
            }
        } else {
            model.playAnimation(animation.getAnimationId());
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (cancelled) return;
                playNext();
            }
        }.runTaskLater(AquaticCrates.instance(),period+ animation.getAnimationLength());
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
