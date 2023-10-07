package cz.larkyy.aquaticcratestesting.nms;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public interface ModelEngineAdapter {

    AdaptedMEModel create(String id, Location location, Player player, Player skin);
    Loader createMEGLoader(JavaPlugin plugin, Runnable runnable);

}
