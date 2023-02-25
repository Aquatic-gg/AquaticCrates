package cz.larkyy.aquaticcratestesting.model.impl;

import cz.larkyy.aquaticcratestesting.loader.impl.AquaticEngineLoader;
import cz.larkyy.aquaticcratestesting.model.Model;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.api.model.holder.ModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;

public class AMEModel extends Model {
    private final ModelHolder modelHolder;
    private final SpawnedModel spawnedModel;
    private final Location location;

    public AMEModel(Location location, ModelHolder modelHolder, SpawnedModel spawnedModel) {
        this.modelHolder = modelHolder;
        this.spawnedModel = spawnedModel;
        this.location = location;
    }


    @Override
    public void playAnimation(String animation) {
        spawnedModel.playAnimation(animation,1);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show(Player player) {
        spawnedModel.show(player);
    }

    @Override
    public void hide(Player player) {
        spawnedModel.hide(player);
    }

    @Override
    public void remove() {
        AquaticModelEngine.getInstance().getModelHandler().deleteHolder(modelHolder);
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public static Model create(String id, Location location, Player player) {
        var holder = AquaticModelEngine.getInstance().getModelHandler().createDummyModelHolder(location);
        var spawnedModel = AquaticModelEngine.getInstance().getModelHandler().spawnModel(holder,id);

        if (player != null) {
            spawnedModel.getRenderHandler().setFilter(p -> p.equals(player));
        }
        spawnedModel.applyModel();

        return new AMEModel(location,holder,spawnedModel);
    }
}
