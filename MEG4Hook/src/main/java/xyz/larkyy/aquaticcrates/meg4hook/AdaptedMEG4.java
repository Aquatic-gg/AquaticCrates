package xyz.larkyy.aquaticcrates.meg4hook;

import cz.larkyy.aquaticcratestesting.nms.AdaptedMEModel;
import cz.larkyy.aquaticcratestesting.nms.Loader;
import cz.larkyy.aquaticcratestesting.nms.ModelEngineAdapter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AdaptedMEG4 implements ModelEngineAdapter {
    @Override
    public AdaptedMEModel create(String id, Location location, Player player) {
        return MEG4Hook.create(id,location,player);
    }

    @Override
    public Loader createMEGLoader(JavaPlugin plugin, Runnable runnable) {
        ModelEngineLoader loader = new ModelEngineLoader(runnable);
        plugin.getServer().getPluginManager().registerEvents(loader,plugin);
        return loader;
    }
}
