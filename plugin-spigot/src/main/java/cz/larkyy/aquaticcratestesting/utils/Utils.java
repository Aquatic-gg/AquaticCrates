package cz.larkyy.aquaticcratestesting.utils;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class Utils {

    public static void playScreenEffect(Player player, int in, int out) {
        player.sendTitle("§fざ","",in,1,out);
    }

    public static boolean areListsSame(List<String> firstList, List<String> secondList) {
        if (firstList.size() != secondList.size()) {
            return false;
        }
        for (int i = 0; i < firstList.size(); i++) {
            if (!firstList.get(i).equals(secondList.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isVectorSame(Vector v1, Vector v2) {
        return (v1.getX() == v2.getX() && v1.getY() == v2.getY() && v1.getZ() == v2.getZ());
    }
}
