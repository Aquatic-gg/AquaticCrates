package cz.larkyy.aquaticcratestesting.hooks;

import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PAPIHook extends PlaceholderExpansion {
    @Override
    public String getAuthor() {
        return "Larkyy";
    }

    @Override
    public String getIdentifier() {
        return "aquaticcrates";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("inanimation")) {
            if (!player.isOnline()) {
                return null;
            }
            Player p = (Player) player;
            CratePlayer cp = CratePlayer.get(p);
            return cp.isInAnimation()+"";
        }
        String[] args = params.split("_");
        switch (args[0].toLowerCase()) {
            case "keys" -> {
                if (!player.isOnline()) {
                    return null;
                }
                if (args.length < 2) {
                    return null;
                }
                CratePlayer cp = CratePlayer.get(player.getPlayer());
                return cp.getKeys(args[1])+"";
            }
        }

        return null;
    }
}
