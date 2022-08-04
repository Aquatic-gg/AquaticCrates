package cz.larkyy.aquaticcratestesting.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerHandler {

    private final Map<OfflinePlayer, CratePlayer> players;

    public PlayerHandler() {
        this.players = new HashMap<>();
    }

    public CratePlayer loadPlayer(Player player) {
        return players.put(player,new CratePlayer(player));
    }

    public void loadPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::loadPlayer);
    }

    public CratePlayer getPlayer(Player player) {
        CratePlayer cp = players.get(player);
        if (cp == null) {
            return loadPlayer(player);
        }
        return cp;
    }

}
