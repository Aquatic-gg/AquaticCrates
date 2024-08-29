package cz.larkyy.aquaticcrates;

import cz.larkyy.aquaticcrates.animation.task.TaskSerializer;
import cz.larkyy.aquaticcrates.animation.task.impl.*;
import cz.larkyy.aquaticcrates.commands.Commands;
import cz.larkyy.aquaticcrates.config.Config;
import cz.larkyy.aquaticcrates.crate.CrateHandler;
import cz.larkyy.aquaticcrates.crate.CrateListener;
import cz.larkyy.aquaticcrates.crate.price.OpenPrices;
import cz.larkyy.aquaticcrates.crate.reward.condition.PermissionCondition;
import cz.larkyy.aquaticcrates.dabatase.DatabaseManager;
import cz.larkyy.aquaticcrates.hologram.HologramHandler;
import cz.larkyy.aquaticcrates.item.ItemHandler;
import cz.larkyy.aquaticcrates.messages.MessageHandler;
import cz.larkyy.aquaticcrates.nms.ModelEngineAdapter;
import cz.larkyy.aquaticcrates.player.PlayerHandler;
import cz.larkyy.aquaticcrates.player.PlayerListener;
import cz.larkyy.aquaticcrates.hooks.PAPIHook;
import gg.aquatic.aquaticseries.lib.AquaticSeriesLib;
import gg.aquatic.aquaticseries.lib.action.ActionTypes;
import gg.aquatic.aquaticseries.lib.awaiters.AbstractAwaiter;
import gg.aquatic.aquaticseries.lib.awaiters.IAAwaiter;
import gg.aquatic.aquaticseries.lib.awaiters.MEGAwaiter;
import gg.aquatic.aquaticseries.lib.format.Format;
import gg.aquatic.aquaticseries.lib.format.color.ColorUtils;
import gg.aquatic.aquaticseries.lib.interactable2.InteractableHandler;
import gg.aquatic.aquaticseries.lib.inventory.lib.InventoryHandler;
import gg.aquatic.aquaticseries.lib.nms.NMSAdapter;
import gg.aquatic.aquaticseries.lib.packet.PacketHandler;
import gg.aquatic.aquaticseries.lib.requirement.RequirementTypes;
import gg.aquatic.aquaticseries.lib.worldobject.WorldObjectHandler;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticcrates.meg3hook.AdaptedMEG3;
import xyz.larkyy.aquaticcrates.meg4hook.AdaptedMEG4;

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
    private static OpenPrices openPrices;
    private static ModelEngineAdapter modelEngineAdapter = null;
    public static boolean configDebug = true;
    public static AquaticSeriesLib aquaticSeriesLib;
    public static HologramHandler hologramHandler;

    public static Format messageFormat;

    @Override
    public void onLoad() {
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Companion.format("&bAquaticCrates &8| &fLoading the plugin..."));
        RequirementTypes.INSTANCE.register("permission", new PermissionCondition());
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

        openPrices = new OpenPrices();
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

            Bukkit.getConsoleSender().sendMessage(ColorUtils.Companion.format("&bAquaticCrates &8| &fLoading &7PlaceholderAPI Hook&f!"));
            new PAPIHook().register();
        }
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Companion.format("&bAquaticCrates &8| &fLoading &7Database&f!"));

        var awaiters = new ArrayList<AbstractAwaiter>();
        var megPlugin = this.getServer().getPluginManager().getPlugin("ModelEngine");

        if (Bukkit.getPluginManager().getPlugin("ModelEngine") != null) {
            var awaiter = new MEGAwaiter(aquaticSeriesLib);
            awaiters.add(awaiter);
            Bukkit.getConsoleSender().sendMessage(ColorUtils.Companion.format("&bAquaticCrates &8| &fLoading &7ModelEngine Hook&f!"));
            awaiter.getFuture().thenRun(() -> {
                awaiters.remove(awaiter);
                if (awaiters.isEmpty()) {
                    Bukkit.getConsoleSender().sendMessage(ColorUtils.Companion.format("&bAquaticCrates &8| &7ModelEngine Hook&f initialized!"));
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
            Bukkit.getConsoleSender().sendMessage(ColorUtils.Companion.format("&bAquaticCrates &8| &fLoading &7ItemsAdder Hook&f!"));
            awaiter.getFuture().thenRun(() -> {
                awaiters.remove(awaiter);
                if (awaiters.isEmpty()) {
                    Bukkit.getConsoleSender().sendMessage(ColorUtils.Companion.format("&bAquaticCrates &8| &7ItemsAdder Hook&f initialized!"));
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
        if (megPlugin != null) {
            var megVersion = megPlugin.getDescription().getVersion();
            if (megVersion.contains("R3.")) {
                modelEngineAdapter = new AdaptedMEG3();
            } else {
                modelEngineAdapter = new AdaptedMEG4();
            }
        }

        try {
            databaseManager.setup();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupAnimationTasks() {
        TaskSerializer.tasks.put("spawnreward", new SpawnRewardTask());
        TaskSerializer.tasks.put("playsound", new PlaySoundTask());
        TaskSerializer.tasks.put("command", new CommandTask());
        TaskSerializer.tasks.put("spawnparticle", new SpawnParticleTask());
        TaskSerializer.tasks.put("sendtitle", new SendTitleTask());
        TaskSerializer.tasks.put("movecamera", new CameraMoveTask());
        TaskSerializer.tasks.put("teleportcamera", new CameraTeleportTask());
    }

    public void load() {
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Companion.format("&bAquaticCrates &8| &fLoading &7Item Database&f!"));
        itemHandler.load();
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Companion.format("&bAquaticCrates &8| &fLoading &7Crates&f!"));
        crateHandler.load();
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Companion.format("&bAquaticCrates &8| &fLoading &7Players&f!"));
        playerHandler.loadPlayers(() -> {
            getServer().getPluginManager().registerEvents(new PlayerListener(),this);
        });
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Companion.format("&bAquaticCrates &8| &fLoading &7Messages!"));
        messageHandler.load();
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Companion.format("&bAquaticCrates &8| &fPlugin &aLoaded&f!"));

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

    public static OpenPrices getOpenPrices() {
        return openPrices;
    }

    /*public static RewardConditions getRewardConditions() {
        return rewardConditions;
    }
     */

    public static ModelEngineAdapter getModelEngineAdapter() {
        return modelEngineAdapter;
    }

    public static HologramHandler getHologramHandler() {
        return hologramHandler;
    }
}
