package cz.larkyy.aquaticcratestesting.model;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public abstract class Model implements IModel {

    public static Model create(String namespace, Location location) {
        String[] strs = namespace.split(":");
        String plugin = strs[0].toLowerCase();
        String id = strs[1];

        switch (plugin) {
            case "modelengine" -> {
                Bukkit.broadcastMessage("Spawning "+id+" from ModelEngine");
                return MEModel.create(id,location);
            }
        }
        return null;
    }

}
