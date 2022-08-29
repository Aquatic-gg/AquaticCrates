package cz.larkyy.aquaticcratestesting.model.impl;

import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import cz.larkyy.aquaticcratestesting.model.Model;
import cz.larkyy.aquaticcratestesting.model.provider.MEProvider;
import org.bukkit.Location;
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
        activeModel.getAnimationHandler().playAnimation(animation,0,0,1);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show(Player player) {

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
        return MEProvider.create(location,id,player);
    }
}
