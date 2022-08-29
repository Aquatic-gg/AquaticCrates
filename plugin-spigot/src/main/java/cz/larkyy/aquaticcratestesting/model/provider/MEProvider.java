package cz.larkyy.aquaticcratestesting.model.provider;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.entity.Dummy;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.model.impl.MEModel;
import cz.larkyy.aquaticcratestesting.model.Model;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class MEProvider {

    public static Model create(Location location, String modelId, Player player) {

        Location loc = location.clone();
        ActiveModel activeModel = ModelEngineAPI.createActiveModel(modelId);
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
