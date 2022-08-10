package cz.larkyy.aquaticcratestesting;

import cz.larkyy.aquaticcratestesting.commands.Commands;
import cz.larkyy.aquaticcratestesting.crate.CrateHandler;
import cz.larkyy.aquaticcratestesting.crate.CrateListener;
import cz.larkyy.aquaticcratestesting.player.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AquaticCratesTesting extends JavaPlugin {

    private static PlayerHandler playerHandler;
    private static CrateHandler crateHandler;

    @Override
    public void onEnable() {
        playerHandler = new PlayerHandler();
        crateHandler = new CrateHandler();
        crateHandler.load();

        getCommand("testcrates").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new CrateListener(),this);
    }

    @Override
    public void onDisable() {
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

    public static AquaticCratesTesting instance() {
        return AquaticCratesTesting.getPlugin(AquaticCratesTesting.class);
    }
}
