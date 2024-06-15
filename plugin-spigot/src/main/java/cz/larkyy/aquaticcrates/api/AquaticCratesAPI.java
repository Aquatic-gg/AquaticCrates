package cz.larkyy.aquaticcrates.api;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.crate.Crate;
import cz.larkyy.aquaticcrates.crate.CrateHandler;
import cz.larkyy.aquaticcrates.crate.Key;
import cz.larkyy.aquaticcrates.player.CratePlayer;
import cz.larkyy.aquaticcrates.player.PlayerHandler;
import org.bukkit.entity.Player;

public class AquaticCratesAPI {

    public static PlayerHandler getPlayerHandler() {
        return AquaticCrates.getPlayerHandler();
    }

    public static CratePlayer getPlayer(Player player) {
        return getPlayerHandler().getPlayer(player);
    }

    public static CrateHandler getCrateHandler() {
        return AquaticCrates.getCrateHandler();
    }

    public static Crate getCrate(String identifier) {
        return getCrateHandler().getCrate(identifier);
    }

    public static Key getKey(String identifier) {
        var crate = getCrate(identifier);
        if (crate == null) return null;
        return crate.getKey();
    }
}
