package cz.larkyy.aquaticcratestesting;

import cz.larkyy.aquaticcratestesting.commands.Commands;
import cz.larkyy.aquaticcratestesting.crate.CrateHandler;
import cz.larkyy.aquaticcratestesting.crate.CrateListener;
import cz.larkyy.aquaticcratestesting.dabatase.DatabaseManager;
import cz.larkyy.aquaticcratestesting.player.PlayerHandler;
import cz.larkyy.aquaticcratestesting.player.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;

public final class AquaticCratesTesting extends JavaPlugin {

    private static PlayerHandler playerHandler;
    private static CrateHandler crateHandler;
    private static DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        crateHandler = new CrateHandler();
        try {
            databaseManager = new DatabaseManager();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        crateHandler.load();

        playerHandler = new PlayerHandler();
        playerHandler.loadPlayers();

        getCommand("testcrates").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new CrateListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerListener(),this);
    }

    @Override
    public void onDisable() {
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
}
