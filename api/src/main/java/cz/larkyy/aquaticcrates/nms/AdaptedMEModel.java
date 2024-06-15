package cz.larkyy.aquaticcrates.nms;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface AdaptedMEModel {

    Location getLocation();
    void remove();
    void hide(Player player);
    void show(Player player);
    void hide();
    void show();
    void playAnimation(String animation);
}
