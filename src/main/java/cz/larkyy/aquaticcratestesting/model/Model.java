package cz.larkyy.aquaticcratestesting.model;

import org.bukkit.Location;

public abstract class Model implements IModel {

    public static Model create(String namespace, Location location) {
        String[] strs = namespace.split(":");
        String plugin = strs[0].toLowerCase();
        String id = strs[1];

        switch (plugin) {
            case "modelengine" -> {
                return MEModel.create(id,location);
            }
        }
        return null;
    }

}
