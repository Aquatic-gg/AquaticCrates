package cz.larkyy.aquaticcrates.model.impl;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.model.Model;
import cz.larkyy.aquaticcrates.nms.AdaptedMEModel;
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

    public static Model create(String id, Location location, Player player, Player skin) {

        AdaptedMEModel meModel = AquaticCrates.getModelEngineAdapter().create(id,location,player,skin);
        return new MEModel(meModel);
    }
}
