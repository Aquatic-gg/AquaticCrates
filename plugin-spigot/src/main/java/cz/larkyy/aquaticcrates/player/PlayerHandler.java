package cz.larkyy.aquaticcrates.player;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.api.AquaticCratesAPI;
import cz.larkyy.aquaticcrates.crate.reroll.Reroll;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;

public class PlayerHandler {

    private final Map<UUID, CratePlayer> players;
    private final Map<Player, Reroll> rerollPlayers;

    public PlayerHandler() {
        this.players = new HashMap<>();
        this.rerollPlayers = new HashMap<>();
    }

    public void loadPlayer(Player player, Consumer<CratePlayer> callback) {
        AquaticCrates.getDatabaseManager().loadPlayer(player,callback);
    }

    public void savePlayer(CratePlayer player) {
        AquaticCrates.getDatabaseManager().savePlayer(player);
    }

    public void savePlayers(boolean async) {
        AquaticCrates.getDatabaseManager().savePlayers(async);
    }

    public void addPlayer(UUID uuid, CratePlayer player) {
        players.put(uuid,player);
    }

    public void loadPlayers(Runnable callback)
    {
        AquaticCrates.getDatabaseManager().loadPlayers(callback);
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
        CratePlayer cp = players.get(player.getUniqueId());
        if (cp == null) {
            cp = new CratePlayer(player.getUniqueId());
            players.put(player.getUniqueId(),cp);
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
