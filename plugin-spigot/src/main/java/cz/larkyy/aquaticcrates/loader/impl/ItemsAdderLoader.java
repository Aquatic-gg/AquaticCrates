package cz.larkyy.aquaticcrates.loader.impl;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.nms.Loader;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ItemsAdderLoader extends Loader implements Listener {
    public ItemsAdderLoader(Runnable runnable) {
        super(runnable);
        AquaticCrates.instance().getServer().getPluginManager().registerEvents(this, AquaticCrates.instance());
    }

    @EventHandler
    public void onModelLoad(ItemsAdderLoadDataEvent e) {
        setLoaded(true);
        getRunnable().run();
    }
}
