package cz.larkyy.aquaticcratestesting.nms;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.function.Consumer;

public interface NMSHandler {

    public enum PlayerInfoAction {
        GAMEMODE
    }

    int spawnEntity(Location l, Consumer<Entity> factory, List<Player> players, String type);

    void despawnEntity(List<Integer> ids, List<Player> players);

    void updateEntity(int id, Consumer<org.bukkit.entity.Entity> factory);

    void throwEntity(int id, Vector vector);

    void teleportEntity(int id, Location location);

    void moveEntity(int id, Location location);

    Entity getEntity(int id);

    void setCamera(int id, Player player);

    void changeGamemode(Player player, GameMode gamemode);

    void setPlayerInfo(String action, Player player, String gameMode);
}
