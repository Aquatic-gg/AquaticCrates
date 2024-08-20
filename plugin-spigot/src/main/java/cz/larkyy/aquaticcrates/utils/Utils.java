package cz.larkyy.aquaticcrates.utils;
import cz.larkyy.aquaticcrates.AquaticCrates;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Utils {

    public static boolean isVectorSame(Vector v1, Vector v2) {
        return (v1.getX() == v2.getX() && v1.getY() == v2.getY() && v1.getZ() == v2.getZ());
    }

    public static String updatePlaceholders(String str, Player player) {
        if (AquaticCrates.instance().getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return PlaceholderAPI.setPlaceholders(player, str);
        } else return str;
    }
}
