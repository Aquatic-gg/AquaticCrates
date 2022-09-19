package cz.larkyy.aquaticcratestesting.model.impl;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.entity.Dummy;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import cz.larkyy.aquaticcratestesting.model.Model;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class MEModel extends Model {

    private final ActiveModel activeModel;
    private final ModeledEntity modeledEntity;
    private final Location location;

    public MEModel(Location location, ModeledEntity modeledEntity, ActiveModel activeModel) {
        this.activeModel = activeModel;
        this.modeledEntity = modeledEntity;
        this.location = location;
    }


    @Override
    public void playAnimation(String animation) {
        activeModel.getAnimationHandler().forceStopAllAnimations();
        activeModel.getAnimationHandler().playAnimation(animation,0,0,1,true);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show(Player player) {
        modeledEntity.showToPlayer(player);
    }

    @Override
    public void hide(Player player) {
        modeledEntity.hideFromPlayer(player);
    }

    @Override
    public void remove() {
        modeledEntity.destroy();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public static Model create(String id, Location location, Player player) {
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
            as.setPersistent(false);
            as.teleport(loc);

            modeledEntity = ModelEngineAPI.createModeledEntity(as);
        }
        modeledEntity.addModel(activeModel,false);

        if (player != null) {
            modeledEntity.getRangeManager().forceSpawn(player);
            //modeledEntity.showToPlayer(player);
        }

        return new MEModel(location,modeledEntity,activeModel);
    }
}
