package cz.larkyy.aquaticcratestesting;


import org.bukkit.entity.Player;

public class Utils {

    public static void playScreenEffect(Player player, int in, int out) {
        player.sendTitle("§fざ","",in,1,out);
    }
}
