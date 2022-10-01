package cz.larkyy.aquaticcratestesting;

import cz.larkyy.aquaticcratestesting.commands.CommandCompleter;
import cz.larkyy.aquaticcratestesting.commands.Commands;
import cz.larkyy.aquaticcratestesting.crate.CrateHandler;
import cz.larkyy.aquaticcratestesting.crate.CrateListener;
import cz.larkyy.aquaticcratestesting.dabatase.DatabaseManager;
import cz.larkyy.aquaticcratestesting.item.ItemHandler;
import cz.larkyy.aquaticcratestesting.messages.MessageHandler;
import cz.larkyy.aquaticcratestesting.nms.NMSHandler;
import cz.larkyy.aquaticcratestesting.player.PlayerHandler;
import cz.larkyy.aquaticcratestesting.player.PlayerListener;
import cz.larkyy.nms.impl.v1_16_R3;
import cz.larkyy.nms.impl.v1_18_R2;
import cz.larkyy.nms.impl.v1_19_R2;
import cz.larkyy.aquaticcratestesting.hooks.PAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.colorutils.Colors;

import java.io.IOException;
import java.sql.SQLException;

public final class AquaticCratesTesting extends JavaPlugin {

    private static PlayerHandler playerHandler;
    private static CrateHandler crateHandler;
    private static DatabaseManager databaseManager;
    private static NMSHandler nmsHandler;
    private static MessageHandler messageHandler;

    private static ItemHandler itemHandler;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fLoading the plugin..."));
        itemHandler = new ItemHandler();
        crateHandler = new CrateHandler();
        playerHandler = new PlayerHandler();
        databaseManager = new DatabaseManager();
        messageHandler = new MessageHandler();

        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fLoading &7NMS Version&f!"));
        String version = "null";
        switch (getServer().getBukkitVersion()) {
            case "1.16.5-R0.1-SNAPSHOT" -> {
                nmsHandler = new v1_16_R3();
                version = "v1_16_R3";
            }
            case "1.18.2-R0.1-SNAPSHOT" -> {
                nmsHandler = new v1_18_R2();
                version = "v1_18_R2";
            }
            case "1.19.2-R0.1-SNAPSHOT" -> {
                nmsHandler = new v1_19_R2();
                version = "v1_19_R2";
            }
        }
        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fUsing NMS version &7"+version+"&f."));

        getCommand("aquaticcrates").setExecutor(new Commands());
        getCommand("aquaticcrates").setTabCompleter(new CommandCompleter());

        getServer().getPluginManager().registerEvents(new CrateListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerListener(),this);

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {

            Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fLoading &7PlaceholderAPI Hook&f!"));
            new PAPIHook().register();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                load();
            }
        }.runTaskLater(this,5);
    }

    public void load() {
        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fLoading &7Database&f!"));
        try {
            databaseManager.setup();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fLoading &7Item Database&f!"));
        itemHandler.load();
        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fLoading &7Crates&f!"));
        crateHandler.load();
        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fLoading &7Players&f!"));
        playerHandler.loadPlayers();
        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fLoading &7Messages!"));
        messageHandler.load();
        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fPlugin &aLoaded&f!"));
    }

    public void unload() {
        crateHandler.unloadCrates();
        playerHandler.savePlayers(false);
        Bukkit.getWorlds().forEach(world -> {
            world.getEntities().forEach(entity -> {
                if ("aquaticcrates".equals(entity.getCustomName())) {
                    entity.setPersistent(false);
                    entity.remove();
                }
            });
        });
        playerHandler.unloadPlayers();
    }

    public void reload() {
        unload();
        load();
    }

    @Override
    public void onDisable() {
        unload();
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

    public static MessageHandler getMessageHandler() {
        return messageHandler;
    }
}
