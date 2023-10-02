package cz.larkyy.aquaticcratestesting.model.impl;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.model.Model;
import cz.larkyy.aquaticcratestesting.nms.AdaptedMEModel;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MEModel extends Model {
    private final AdaptedMEModel adaptedMEModel;

    public MEModel(AdaptedMEModel adaptedMEModel) {
        this.adaptedMEModel = adaptedMEModel;
    }

    @Override
    public void playAnimation(String animation) {
        adaptedMEModel.playAnimation(animation);
    }

    @Override
    public void show() {
        adaptedMEModel.show();
    }

    @Override
    public void hide() {
        adaptedMEModel.hide();
    }

    @Override
    public void show(Player player) {
        adaptedMEModel.show(player);
    }

    @Override
    public void hide(Player player) {
        adaptedMEModel.hide(player);
    }

    @Override
    public void remove() {
        adaptedMEModel.remove();
    }

    @Override
    public Location getLocation() {
        return adaptedMEModel.getLocation();
    }

    public static Model create(String id, Location location, Player player) {

        AdaptedMEModel meModel = AquaticCratesTesting.getModelEngineAdapter().create(id,location,player);
        return new MEModel(meModel);
    }
}
