package xyz.larkyy.aquaticcrates.meg3hook;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.entity.Dummy;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import cz.larkyy.aquaticcrates.nms.AdaptedMEModel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MEG3Hook implements AdaptedMEModel {
    private final String modelId;

    private final UUID uuid;
    private final Location location;

    public MEG3Hook(Location location, UUID uuid, String modelId) {
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
        getActiveModel().getAnimationHandler().forceStopAllAnimations();
        getActiveModel().getAnimationHandler().playAnimation(animation,0,0,1,true);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void show(Player player) {
        getModeledEntity().showToPlayer(player);
    }

    @Override
    public void hide(Player player) {
        getModeledEntity().hideFromPlayer(player);
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

        if (player != null) {
            Dummy dummy = ModelEngineAPI.createDummy();
            dummy.setLocation(loc);

            dummy.setYBodyRot(loc.getYaw());
            dummy.setYHeadRot(loc.getYaw());
            dummy.setXHeadRot(loc.getPitch());
            modeledEntity = ModelEngineAPI.createModeledEntity(dummy);
        } else {
            Entity mob = loc.getWorld().spawnEntity(loc.clone().add(0,-3,0), EntityType.ARMOR_STAND);
            ArmorStand as = (ArmorStand) mob;
            as.setInvisible(true);
            as.setCustomName("aquaticcrates");
            as.setMarker(true);
            as.teleport(loc);

            modeledEntity = ModelEngineAPI.createModeledEntity(as);
        }
        modeledEntity.addModel(activeModel,false);

        if (player != null) {
            modeledEntity.getRangeManager().forceSpawn(player);
            //modeledEntity.showToPlayer(player);
        }

        return new MEG3Hook(location,modeledEntity.getBase().getUniqueId(), id);
    }

}
