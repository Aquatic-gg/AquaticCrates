package cz.larkyy.aquaticcratestesting.model.impl;
import cz.larkyy.aquaticcratestesting.model.Model;
import dev.lone.itemsadder.api.CustomEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class IAModel extends Model {
    private final CustomEntity entity;

    public IAModel(CustomEntity entity) {
        this.entity = entity;
    }


    @Override
    public void playAnimation(String animation) {
        entity.stopAnimation();
        entity.playAnimation(animation);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show(Player player) {
        entity.addViewer(player);
    }

    @Override
    public void hide(Player player) {
        entity.removeViewer(player);
    }

    @Override
    public void remove() {
        entity.destroy();
    }

    @Override
    public Location getLocation() {
        return entity.getLocation();
    }

    public static Model create(String id, Location location, Player player) {
        CustomEntity entity;
        if (player == null) {
             entity = CustomEntity.spawn(id,location,true,false,true);
        } else {
            entity = CustomEntity.spawn(id,location, Arrays.asList(player),true,false,true,le -> {});
        }
        return new IAModel(entity);
    }
}
