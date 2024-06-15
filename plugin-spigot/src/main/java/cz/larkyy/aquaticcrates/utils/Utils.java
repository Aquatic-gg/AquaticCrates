package cz.larkyy.aquaticcrates.utils;
import org.bukkit.util.Vector;

public class Utils {

    public static boolean isVectorSame(Vector v1, Vector v2) {
        return (v1.getX() == v2.getX() && v1.getY() == v2.getY() && v1.getZ() == v2.getZ());
    }
}
