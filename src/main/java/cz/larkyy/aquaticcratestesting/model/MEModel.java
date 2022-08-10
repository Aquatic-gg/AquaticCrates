package cz.larkyy.aquaticcratestesting.model;

import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import cz.larkyy.aquaticcratestesting.model.provider.MEProvider;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MEModel extends Model {

    private final ActiveModel activeModel;
    private final ModeledEntity modeledEntity;

    public MEModel(ModeledEntity modeledEntity, ActiveModel activeModel) {
        this.activeModel = activeModel;
        this.modeledEntity = modeledEntity;
    }


    @Override
    public void playAnimation(String animation) {
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

    }

    @Override
    public void remove() {
        modeledEntity.removeModel(activeModel.getBlueprint().getModelId());
    }

    public static Model create(String id, Location location) {
        return MEProvider.create(location,id);
    }
}
