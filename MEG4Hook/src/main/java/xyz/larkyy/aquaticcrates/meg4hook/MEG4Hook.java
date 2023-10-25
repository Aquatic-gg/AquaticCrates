package xyz.larkyy.aquaticcrates.meg4hook;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import com.ticxo.modelengine.api.animation.handler.IPriorityHandler;
import com.ticxo.modelengine.api.animation.handler.IStateMachineHandler;
import com.ticxo.modelengine.api.entity.Dummy;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import com.ticxo.modelengine.api.model.bone.BoneBehaviorTypes;
import cz.larkyy.aquaticcratestesting.nms.AdaptedMEModel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MEG4Hook implements AdaptedMEModel {

    private final String modelId;

    private final UUID uuid;
    private final Location location;

    public MEG4Hook(Location location, UUID uuid, String modelId) {
        this.modelId = modelId;
        this.uuid = uuid;
        this.location = location;
    }

    private ModeledEntity getModeledEntity() {
        return ModelEngineAPI.getModeledEntity(uuid);
    }

    private ActiveModel getActiveModel() {
        return getModeledEntity().getModels().values().stream().findFirst().get();
    }

    @Override
    public void playAnimation(String animation) {
        AnimationHandler animationHandler = getActiveModel().getAnimationHandler();
        animationHandler.forceStopAllAnimations();
        animationHandler.playAnimation(animation,0,0,1,false);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void show(Player player) {
        var base = getModeledEntity().getBase();
        if (!(base instanceof Dummy<?> dummy)) {
            return;
        }
        dummy.setForceHidden(player,false);
    }

    @Override
    public void hide(Player player) {
        var base = getModeledEntity().getBase();
        if (!(base instanceof Dummy<?> dummy)) {
            return;
        }
        dummy.setForceHidden(player,true);
    }

    @Override
    public void remove() {
        var me = getModeledEntity();
        if (me != null) me.destroy();

        var e = Bukkit.getEntity(uuid);
        if (e != null) e.remove();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public static AdaptedMEModel create(String id, Location location, Player player, Player skin) {
        Location loc = location.clone();
        ActiveModel activeModel = ModelEngineAPI.createActiveModel(id);
        ModeledEntity modeledEntity;

        Dummy<?> dummy = new Dummy<>();
        dummy.setLocation(loc);

        dummy.getBodyRotationController().setYBodyRot(loc.getYaw());
        dummy.setYHeadRot(loc.getYaw());
        dummy.setXHeadRot(loc.getPitch());
        /*
        dummy.setYBodyRot(loc.getYaw());
         */
        modeledEntity = ModelEngineAPI.createModeledEntity(dummy);
        modeledEntity.addModel(activeModel,false);

        if (player != null) {
            dummy.setDetectingPlayers(false);
            dummy.setForceViewing(player, true);
        }

        if (skin != null) {
            activeModel.getBones().forEach((s, modelBone) -> {
                modelBone.getBoneBehavior(BoneBehaviorTypes.PLAYER_LIMB).ifPresent(playerLimb -> {
                    playerLimb.setTexture(skin);
                });
            });
        }

        return new MEG4Hook(location,modeledEntity.getBase().getUUID(), id);
    }

}
