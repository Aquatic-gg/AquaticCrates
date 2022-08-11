package cz.larkyy.aquaticcratestesting.player;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PlayerHandler {

    private final Map<OfflinePlayer, CratePlayer> players;

    public PlayerHandler() {
        this.players = new HashMap<>();
    }

    public void loadPlayer(Player player, Consumer<CratePlayer> callback) {
        AquaticCratesTesting.getDatabaseManager().loadPlayer(player,callback);
    }

    public void savePlayer(CratePlayer player) {
        AquaticCratesTesting.getDatabaseManager().savePlayer(player);
    }

    public void savePlayers(boolean async) {
        AquaticCratesTesting.getDatabaseManager().savePlayers(async);
    }

    public void addPlayer(OfflinePlayer offlinePlayer, CratePlayer player) {
        players.put(offlinePlayer,player);
    }

    public void loadPlayers() {
        Bukkit.getOnlinePlayers().forEach(p -> loadPlayer(p, cp -> {}));
    }

    public List<CratePlayer> getPlayers() {
        return new ArrayList<>(players.values());
    }

    public CratePlayer getPlayer(Player player) {
        CratePlayer cp = players.get(player);
        if (cp == null) {
            cp = new CratePlayer(player);
            players.put(player,cp);
        }
        return cp;
    }

}
