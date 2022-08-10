package cz.larkyy.aquaticcratestesting.model.provider;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import cz.larkyy.aquaticcratestesting.model.MEModel;
import cz.larkyy.aquaticcratestesting.model.IModel;
import cz.larkyy.aquaticcratestesting.model.Model;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class MEProvider {

    public static Model create(Location location, String modelId) {

        Location loc = location.clone();
        loc.setYaw(loc.getYaw()+180);

        Entity mob = loc.getWorld().spawnEntity(loc.clone().add(0,-3,0), EntityType.ARMOR_STAND);
        ArmorStand as = (ArmorStand) mob;
        as.setInvisible(true);
        as.setCustomName("aquaticcrates");
        as.setMarker(true);
        as.setPersistent(false);
        as.teleport(loc);

        ActiveModel activeModel = ModelEngineAPI.createActiveModel(modelId);
        ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(as);

        modeledEntity.addModel(activeModel,false);

        return new MEModel(modeledEntity,activeModel);
    }

}
