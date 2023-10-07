package xyz.larkyy.aquaticcrates.meg3hook;

import cz.larkyy.aquaticcratestesting.nms.AdaptedMEModel;
import cz.larkyy.aquaticcratestesting.nms.Loader;
import cz.larkyy.aquaticcratestesting.nms.ModelEngineAdapter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AdaptedMEG3 implements ModelEngineAdapter {
    @Override
    public AdaptedMEModel create(String id, Location location, Player player, Player skin) {
        return MEG3Hook.create(id,location,player, skin);
    }

    @Override
    public Loader createMEGLoader(JavaPlugin plugin, Runnable runnable) {
        ModelEngineLoader loader = new ModelEngineLoader(runnable);
        plugin.getServer().getPluginManager().registerEvents(loader,plugin);
        return loader;
    }
}
