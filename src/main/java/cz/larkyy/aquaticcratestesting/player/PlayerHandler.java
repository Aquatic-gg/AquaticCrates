package cz.larkyy.aquaticcratestesting.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerHandler {

    private final Map<OfflinePlayer, CratePlayer> players;

    public PlayerHandler() {
        this.players = new HashMap<>();
    }

    public CratePlayer loadPlayer(Player player) {
        CratePlayer cp = new CratePlayer(player);
        players.put(player,cp);
        return cp;
    }

    public void loadPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::loadPlayer);
    }

    public List<CratePlayer> getPlayers() {
        return new ArrayList<>(players.values());
    }

    public CratePlayer getPlayer(Player player) {
        CratePlayer cp = players.get(player);
        if (cp == null) {
            return loadPlayer(player);
        }
        return cp;
    }

}
