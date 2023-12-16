package cz.larkyy.aquaticcratestesting;

import cz.larkyy.aquaticcratestesting.animation.task.Tasks;
import cz.larkyy.aquaticcratestesting.commands.CommandCompleter;
import cz.larkyy.aquaticcratestesting.commands.Commands;
import cz.larkyy.aquaticcratestesting.crate.CrateHandler;
import cz.larkyy.aquaticcratestesting.crate.CrateListener;
import cz.larkyy.aquaticcratestesting.crate.price.OpenPrices;
import cz.larkyy.aquaticcratestesting.crate.reward.RewardActions;
import cz.larkyy.aquaticcratestesting.crate.reward.condition.RewardConditions;
import cz.larkyy.aquaticcratestesting.dabatase.DatabaseManager;
import cz.larkyy.aquaticcratestesting.editor.EditingHandler;
import cz.larkyy.aquaticcratestesting.item.ItemHandler;
import cz.larkyy.aquaticcratestesting.loader.LoaderManager;
import cz.larkyy.aquaticcratestesting.messages.MessageHandler;
import cz.larkyy.aquaticcratestesting.nms.ModelEngineAdapter;
import cz.larkyy.aquaticcratestesting.nms.NMSHandler;
import cz.larkyy.aquaticcratestesting.player.PlayerHandler;
import cz.larkyy.aquaticcratestesting.player.PlayerListener;
import cz.larkyy.aquaticcratestesting.utils.colors.Colors;
import cz.larkyy.nms.impl.impl.v1_16_R3;
import cz.larkyy.nms.impl.impl.v1_17_R1;
import cz.larkyy.nms.impl.impl.v1_18_R2;
import cz.larkyy.nms.impl.impl.v1_19_R2;
import cz.larkyy.aquaticcratestesting.hooks.PAPIHook;
import cz.larkyy.nms.impl.v1_19_R3;
import cz.larkyy.nms.impl.v1_20_R1;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticcrates.meg3hook.AdaptedMEG3;
import xyz.larkyy.aquaticcrates.meg3hook.MEG3Hook;
import xyz.larkyy.aquaticcrates.meg4hook.AdaptedMEG4;
import xyz.larkyy.aquaticemotes.nms_v1_20_2.NMS_v1_20_2;
import xyz.larkyy.nms.v1_19_4.V1_19_4;

import java.io.IOException;
import java.sql.SQLException;

public final class AquaticCratesTesting extends JavaPlugin {

    private static PlayerHandler playerHandler;
    private static Tasks tasks;
    private static RewardActions rewardActions;
    private static CrateHandler crateHandler;
    private static DatabaseManager databaseManager;
    private static NMSHandler nmsHandler;
    private static MessageHandler messageHandler;

    private static ItemHandler itemHandler;
    private static OpenPrices openPrices;
    private static RewardConditions rewardConditions;
    private static EditingHandler editingHandler;
    private static ModelEngineAdapter modelEngineAdapter = null;
    public static boolean loaded = false;

    @Override
    public void onLoad() {
        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fLoading the plugin..."));
        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fLoading &7NMS Version&f!"));
        String version = "null";
        switch (getServer().getBukkitVersion()) {
            case "1.16.5-R0.1-SNAPSHOT" -> {
                nmsHandler = new v1_16_R3();
                version = "v1_16_R3";
            }
            case "1.17.1-R0.1-SNAPSHOT" -> {
                nmsHandler = new v1_17_R1();
                version = "v1_17_R1";
            }
            case "1.18.2-R0.1-SNAPSHOT" -> {
                nmsHandler = new v1_18_R2();
                version = "v1_18_R2";
            }
            case "1.19.2-R0.1-SNAPSHOT" -> {
                nmsHandler = new v1_19_R2();
                version = "v1_19_R2";
            }
            case "1.19.3-R0.1-SNAPSHOT" -> {
                nmsHandler = new v1_19_R3();
                version = "v1_19_R3";
            }
            case "1.19.4-R0.1-SNAPSHOT" -> {
                nmsHandler = new V1_19_4();
                version = "v1_19_R4";
            }
            case "1.20.1-R0.1-SNAPSHOT", "1.20-R0.1-SNAPSHOT" -> {
                nmsHandler = new v1_20_R1();
                version = "v1_20_R1";
            }
            case "1.20.2-R0.1-SNAPSHOT" -> {
                nmsHandler = new NMS_v1_20_2(this);
                version = "v1_20_R2";
            }
        }
        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fUsing NMS version &7"+version+"&f."));
    }

    @Override
    public void onEnable() {

        tasks = new Tasks();
        openPrices = new OpenPrices();
        rewardConditions = new RewardConditions();
        rewardActions = new RewardActions();
        itemHandler = new ItemHandler();
        crateHandler = new CrateHandler();
        playerHandler = new PlayerHandler();
        databaseManager = new DatabaseManager();
        messageHandler = new MessageHandler();
        editingHandler = new EditingHandler();

        Metrics metrics = new Metrics(this, 19254);

        var megPlugin = this.getServer().getPluginManager().getPlugin("ModelEngine");
        if (megPlugin != null) {
            var megVersion = megPlugin.getDescription().getVersion();
            if (megVersion.contains("R3.")) {
                modelEngineAdapter = new AdaptedMEG3();
            } else {
                modelEngineAdapter = new AdaptedMEG4();
            }
        }

        getCommand("aquaticcrates").setExecutor(new Commands());
        getCommand("aquaticcrates").setTabCompleter(new CommandCompleter());

        getServer().getPluginManager().registerEvents(new CrateListener(),this);
        getServer().getPluginManager().registerEvents(new EditingHandler(),this);

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {

            Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fLoading &7PlaceholderAPI Hook&f!"));
            new PAPIHook().register();
        }
        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fLoading &7Database&f!"));
        try {
            databaseManager.setup();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        new LoaderManager(
                () -> new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (loaded) {
                            unload();
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                load();
                                loaded = true;
                            }
                        }.runTaskLater(instance(),1);
                    }
                }.runTask(this)
        );
    }

    public void load() {
        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fLoading &7Item Database&f!"));
        itemHandler.load();
        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fLoading &7Crates&f!"));
        crateHandler.load();
        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fLoading &7Players&f!"));
        playerHandler.loadPlayers(() -> {
            getServer().getPluginManager().registerEvents(new PlayerListener(),this);
        });
        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fLoading &7Messages!"));
        messageHandler.load();
        Bukkit.getConsoleSender().sendMessage(Colors.format("&bAquaticCrates &8| &fPlugin &aLoaded&f!"));

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

    public static Tasks getTasks() {
        return tasks;
    }

    public static RewardActions getRewardActions() {
        return rewardActions;
    }

    public static OpenPrices getOpenPrices() {
        return openPrices;
    }

    public static RewardConditions getRewardConditions() {
        return rewardConditions;
    }

    public static EditingHandler getEditingHandler() {
        return editingHandler;
    }

    public static ModelEngineAdapter getModelEngineAdapter() {
        return modelEngineAdapter;
    }
}
