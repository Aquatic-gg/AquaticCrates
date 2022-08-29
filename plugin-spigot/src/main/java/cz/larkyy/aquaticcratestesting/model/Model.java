package cz.larkyy.aquaticcratestesting.model;

import cz.larkyy.aquaticcratestesting.model.impl.EmptyModel;
import cz.larkyy.aquaticcratestesting.model.impl.MEModel;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Model implements IModel {

    public static Model create(String namespace, Location location, Player player) {
        if (namespace == null || !namespace.contains(":")) {
            return new EmptyModel(location);
        }

        String[] strs = namespace.split(":");
        String plugin = strs[0].toLowerCase();
        String id = strs[1];

        switch (plugin) {
            case "modelengine": {
                return MEModel.create(id,location,player);
            }
        }
        return new EmptyModel(location);
    }

    @Override
    public void playAnimation(String animation) {
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

    }
}
