package cz.larkyy.aquaticcratestesting;

import cz.larkyy.aquaticcratestesting.commands.Commands;
import cz.larkyy.aquaticcratestesting.crate.CrateHandler;
import cz.larkyy.aquaticcratestesting.crate.CrateListener;
import cz.larkyy.aquaticcratestesting.dabatase.DatabaseManager;
import cz.larkyy.aquaticcratestesting.item.ItemHandler;
import cz.larkyy.aquaticcratestesting.nms.NMSHandler;
import cz.larkyy.aquaticcratestesting.player.PlayerHandler;
import cz.larkyy.aquaticcratestesting.player.PlayerListener;
import cz.larkyy.nms.impl.v1_18_R2;
import cz.larkyy.nms.impl.v1_19_R2;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.sql.SQLException;

public final class AquaticCratesTesting extends JavaPlugin {

    private static PlayerHandler playerHandler;
    private static CrateHandler crateHandler;
    private static DatabaseManager databaseManager;
    private static NMSHandler nmsHandler;

    private static ItemHandler itemHandler;

    @Override
    public void onEnable() {
        itemHandler = new ItemHandler();
        crateHandler = new CrateHandler();
        try {
            databaseManager = new DatabaseManager();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        playerHandler = new PlayerHandler();
        switch (getServer().getMinecraftVersion()) {
            case "1.18.2": {
                nmsHandler = new v1_18_R2();
            }
            case "1.19.2": {
                nmsHandler = new v1_19_R2();
            }
        }

        getCommand("testcrates").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new CrateListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerListener(),this);

        new BukkitRunnable() {
            @Override
            public void run() {
                load();
            }
        }.runTaskLater(this,5);
    }

    public void load() {
        itemHandler.load();
        crateHandler.load();
        playerHandler.loadPlayers();
    }

    public void unload() {
        crateHandler.unloadCrates();
    }

    @Override
    public void onDisable() {
        unload();

        playerHandler.savePlayers(false);
        Bukkit.getWorlds().forEach(world -> {
            world.getEntities().forEach(entity -> {
                if ("aquaticcrates".equals(entity.getCustomName())) {
                    entity.setPersistent(false);
                    entity.remove();
                }
            });
        });
    }

    public static PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    public static CrateHandler getCrateHandler() {
        return crateHandler;
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    public static AquaticCratesTesting instance() {
        return AquaticCratesTesting.getPlugin(AquaticCratesTesting.class);
    }

    public static NMSHandler getNmsHandler() {
        return nmsHandler;
    }

    public static ItemHandler getItemHandler() {
        return itemHandler;
    }
}
