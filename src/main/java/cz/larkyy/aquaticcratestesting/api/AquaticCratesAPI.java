package cz.larkyy.aquaticcratestesting.api;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.crate.CrateHandler;
import cz.larkyy.aquaticcratestesting.crate.Key;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import cz.larkyy.aquaticcratestesting.player.PlayerHandler;
import org.bukkit.entity.Player;

public class AquaticCratesAPI {

    public static PlayerHandler getPlayerHandler() {
        return AquaticCratesTesting.getPlayerHandler();
    }

    public static CratePlayer getPlayer(Player player) {
        return getPlayerHandler().getPlayer(player);
    }

    public static CrateHandler getCrateHandler() {
        return AquaticCratesTesting.getCrateHandler();
    }

    public static Crate getCrate(String identifier) {
        return getCrateHandler().getCrate(identifier);
    }

    public static Key getKey(String identifier) {
        return getCrate(identifier).getKey();
    }
}
