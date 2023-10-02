package cz.larkyy.aquaticcratestesting.loader.impl;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.nms.Loader;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ItemsAdderLoader extends Loader implements Listener {
    public ItemsAdderLoader(Runnable runnable) {
        super(runnable);
        AquaticCratesTesting.instance().getServer().getPluginManager().registerEvents(this,AquaticCratesTesting.instance());
    }

    @EventHandler
    public void onModelLoad(ItemsAdderLoadDataEvent e) {
        setLoaded(true);
        getRunnable().run();
    }
}
