package cz.larkyy.aquaticcratestesting.model;

import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;

public class MEModel implements Model {

    private final ActiveModel activeModel;
    private final ModeledEntity modeledEntity;

    public MEModel(ModeledEntity modeledEntity, ActiveModel activeModel) {
        this.activeModel = activeModel;
        this.modeledEntity = modeledEntity;
    }


}
