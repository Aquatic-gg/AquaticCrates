package cz.larkyy.aquaticcrates;

import cz.larkyy.aquaticcrates.animation.task.TaskSerializer;
import cz.larkyy.aquaticcrates.animation.task.impl.*;
import cz.larkyy.aquaticcrates.commands.Commands;
import cz.larkyy.aquaticcrates.config.Config;
import cz.larkyy.aquaticcrates.crate.CrateHandler;
import cz.larkyy.aquaticcrates.crate.CrateListener;
import cz.larkyy.aquaticcrates.crate.price.types.KeyPrice;
import cz.larkyy.aquaticcrates.crate.reward.condition.KeyCondition;
import cz.larkyy.aquaticcrates.crate.reward.condition.PermissionCondition;
import cz.larkyy.aquaticcrates.dabatase.DatabaseManager;
import cz.larkyy.aquaticcrates.hologram.HologramHandler;
import cz.larkyy.aquaticcrates.item.ItemHandler;
import cz.larkyy.aquaticcrates.messages.MessageHandler;
import cz.larkyy.aquaticcrates.player.PlayerHandler;
import cz.larkyy.aquaticcrates.player.PlayerListener;
import cz.larkyy.aquaticcrates.hooks.PAPIHook;
import gg.aquatic.aquaticseries.lib.AquaticSeriesLib;
import gg.aquatic.aquaticseries.lib.awaiters.AbstractAwaiter;
import gg.aquatic.aquaticseries.lib.awaiters.IAAwaiter;
import gg.aquatic.aquaticseries.lib.awaiters.MEGAwaiter;
import gg.aquatic.aquaticseries.lib.betterinventory2.InventoryHandler;
import gg.aquatic.aquaticseries.lib.format.Format;
import gg.aquatic.aquaticseries.lib.format.color.ColorUtils;
import gg.aquatic.aquaticseries.lib.interactable2.InteractableHandler;
import gg.aquatic.aquaticseries.lib.logger.type.InfoLogger;
import gg.aquatic.aquaticseries.lib.nms.NMSAdapter;
import gg.aquatic.aquaticseries.lib.packet.PacketHandler;
import gg.aquatic.aquaticseries.lib.price.PriceTypes;
import gg.aquatic.aquaticseries.lib.requirement.RequirementTypes;
import gg.aquatic.aquaticseries.lib.worldobject.WorldObjectHandler;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class AquaticCrates extends JavaPlugin {

    private static PlayerHandler playerHandler;
    private static CrateHandler crateHandler;
    private static DatabaseManager databaseManager;
    private static MessageHandler messageHandler;

    private static ItemHandler itemHandler;
    public static boolean configDebug = true;
    public static AquaticSeriesLib aquaticSeriesLib;
    public static HologramHandler hologramHandler;

    public static Format messageFormat;

    @Override
    public void onLoad() {
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Companion.format("&fLoading the plugin..."));
        RequirementTypes.INSTANCE.register("permission", new PermissionCondition());
        RequirementTypes.INSTANCE.register("hascratekey", new KeyCondition());
        setupAnimationTasks();

        var config = new Config(this,"config.yml");
        config.load();
        var cfg = config.getConfiguration();

        configDebug = cfg.getBoolean("config-debug-messages",true);
        messageFormat = Format.valueOf(cfg.getString("message-format","LEGACY").toUpperCase());
    }

    @Override
    public void onEnable() {
        aquaticSeriesLib = AquaticSeriesLib.Companion.init(this, List.of(
                InventoryHandler.INSTANCE, InteractableHandler.INSTANCE, PacketHandler.INSTANCE, WorldObjectHandler.INSTANCE
        ));
        //CustomItem.Companion.getCustomItemHandler().getItemRegistry();
        aquaticSeriesLib.setMessageFormatting(messageFormat);
        //rewardConditions = new RewardConditions();
        itemHandler = new ItemHandler();
        crateHandler = new CrateHandler();
        playerHandler = new PlayerHandler();
        databaseManager = new DatabaseManager();
        messageHandler = new MessageHandler();
        hologramHandler = new HologramHandler();

        new Metrics(this, 19254);

        var cmds = new Commands();
        getCommand("aquaticcrates").setExecutor(cmds);
        getCommand("aquaticcrates").setTabCompleter(cmds);

        getServer().getPluginManager().registerEvents(new CrateListener(),this);

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            InfoLogger.INSTANCE.send("&fLoading &7PlaceholderAPI Hook&f!");
            new PAPIHook().register();
        }
        InfoLogger.INSTANCE.send("&fLoading &7Database&f!");

        var awaiters = new ArrayList<AbstractAwaiter>();
        //var megPlugin = this.getServer().getPluginManager().getPlugin("ModelEngine");

        if (Bukkit.getPluginManager().getPlugin("ModelEngine") != null) {
            var awaiter = new MEGAwaiter(aquaticSeriesLib);
            awaiters.add(awaiter);
            InfoLogger.INSTANCE.send("&fLoading &7ModelEngine Hook&f!");
            awaiter.getFuture().thenRun(() -> {
                awaiters.remove(awaiter);
                if (awaiters.isEmpty()) {
                    InfoLogger.INSTANCE.send("&7ModelEngine Hook&f initialized!");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            load();
                        }
                    }.runTaskLater(this, 1L);
                }
            });
        }
        if (Bukkit.getPluginManager().getPlugin("ItemsAdder") != null) {
            var awaiter = new IAAwaiter(aquaticSeriesLib);
            awaiters.add(awaiter);
            InfoLogger.INSTANCE.send("&fLoading &7ItemsAdder Hook&f!");
            awaiter.getFuture().thenRun(() -> {
                awaiters.remove(awaiter);
                if (awaiters.isEmpty()) {
                    InfoLogger.INSTANCE.send("&7ItemsAdder Hook&f initialized!");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            load();
                        }
                    }.runTaskLater(this, 1L);
                }
            });
        }
        if (awaiters.stream().allMatch(AbstractAwaiter::getLoaded)) {
            load();
        }

        try {
            databaseManager.setup();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupAnimationTasks() {
        PriceTypes.INSTANCE.getTypes().put("cratekey", new KeyPrice());

        TaskSerializer.tasks.put("spawnreward", new SpawnRewardTask());
        TaskSerializer.tasks.put("playsound", new PlaySoundTask());
        TaskSerializer.tasks.put("command", new CommandTask());
        TaskSerializer.tasks.put("spawnparticle", new SpawnParticleTask());
        TaskSerializer.tasks.put("sendtitle", new SendTitleTask());
        TaskSerializer.tasks.put("movecamera", new CameraMoveTask());
        TaskSerializer.tasks.put("teleportcamera", new CameraTeleportTask());
    }

    public void load() {
        InfoLogger.INSTANCE.send("&fLoading &7Item &7Database&f!");
        itemHandler.load();
        InfoLogger.INSTANCE.send("&fLoading &7Crates&f!");
        crateHandler.load();
        InfoLogger.INSTANCE.send("&fLoading &7Players&f!");
        playerHandler.loadPlayers(() -> {
            getServer().getPluginManager().registerEvents(new PlayerListener(),this);
        });
        InfoLogger.INSTANCE.send("&fLoading &7Messages&f!");
        messageHandler.load();
        InfoLogger.INSTANCE.send("&fPlugin &aLoaded&f!");

        List<String> lines = List.of(
                "&b                                _   _       _____           _                 ",
                "&b         /\\                    | | (_)     / ____|         | |                ",
                "&b        /  \\   __ _ _   _  __ _| |_ _  ___| |     _ __ __ _| |_ ___  ___      ",
                "&b       / /\\ \\ / _` | | | |/ _` | __| |/ __| |    | '__/ _` | __/ _ \\/ __|     ",
                "&b      / ____ \\ (_| | |_| | (_| | |_| | (__| |____| | | (_| | ||  __/\\__ \\     ",
                "&b     /_/    \\_\\__, |\\__,_|\\__,_|\\__|_|\\___|\\_____|_|  \\__,_|\\__\\___||___/     ",
                "&b                 | |                                                          ",
                "&b                 |_|                                                          ",
                "",
                "  &7Created by &3Aquatic Creations",
                "  &7Discord: &3&o&nhttp://discord.aquatic.gg",
                ""
        );

        for (String line : lines) {
            Bukkit.getConsoleSender().sendMessage(ColorUtils.Companion.format(line));
        }



        //AquaticModelEngine.getInstance().getModelGenerator().generateModels();
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
        Bukkit.getLogger().info("DISABLING");
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
    public static AquaticCrates instance() {
        return AquaticCrates.getPlugin(AquaticCrates.class);
    }

    public static NMSAdapter getNmsHandler() {
        return aquaticSeriesLib.getNmsAdapter();
    }

    public static ItemHandler getItemHandler() {
        return itemHandler;
    }

    public static MessageHandler getMessageHandler() {
        return messageHandler;
    }

    /*public static RewardConditions getRewardConditions() {
        return rewardConditions;
    }
     */

    public static HologramHandler getHologramHandler() {
        return hologramHandler;
    }
}
