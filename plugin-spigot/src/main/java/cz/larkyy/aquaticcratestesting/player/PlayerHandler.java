package cz.larkyy.aquaticcratestesting.player;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.api.AquaticCratesAPI;
import cz.larkyy.aquaticcratestesting.crate.reroll.Reroll;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PlayerHandler {

    private final Map<OfflinePlayer, CratePlayer> players;
    private final Map<Player, Reroll> rerollPlayers;

    public PlayerHandler() {
        this.players = new HashMap<>();
        this.rerollPlayers = new HashMap<>();
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

    public void loadPlayers(Runnable callback)
    {
        AquaticCratesTesting.getDatabaseManager().loadPlayers(callback);
    }

    public void unloadPlayer(OfflinePlayer p) {
        players.remove(p);
    }

    public void unloadPlayers() {
        players.clear();
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

    public Reroll getRerollPlayer(Player p) {
        return rerollPlayers.get(p);
    }

    public void addRerollPlayer(Reroll rp) {
        rerollPlayers.put(rp.getPlayer(),rp);
    }
    public void removeRerollPlayer(Player p) {
        rerollPlayers.remove(p);
    }

    public boolean isInAnimation(Player p) {
        return AquaticCratesAPI.getCrateHandler().isInAnimation(p);
    }
}
