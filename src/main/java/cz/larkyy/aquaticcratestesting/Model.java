package cz.larkyy.aquaticcratestesting;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class Model {

    private final ModeledEntity modeledEntity;
    private final String model;

    public Model(ModeledEntity modeledEntity, String model) {
        this.modeledEntity = modeledEntity;
        this.model = model;
    }

    public void playAnimation(String animation) {
        modeledEntity.getActiveModel(model).addState(animation,0,0,1);
    }

    public void despawn() {
        modeledEntity.removeModel(model);
        modeledEntity.getEntity().remove();
    }

    public static Model spawn(String modelId, Location location) {
        Location loc = location.clone();

        Entity mob = loc.getWorld().spawnEntity(loc.clone().add(0,-3,0), EntityType.ARMOR_STAND);
        ArmorStand as = (ArmorStand) mob;
        as.setInvisible(true);
        as.setCustomName("aquaticcrates");
        as.setMarker(true);
        as.setPersistent(false);
        as.teleport(loc);

        ActiveModel model = ModelEngineAPI.api.getModelManager().createActiveModel(modelId);
        model.setClamp(0);

        ModeledEntity modeledEntity = ModelEngineAPI.api.getModelManager().createModeledEntity(mob);

        modeledEntity.addActiveModel(model);
        modeledEntity.setInvisible(true);

        return new Model(modeledEntity,modelId);
    }

}
