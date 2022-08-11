package cz.larkyy.aquaticcratestesting.dabatase.drivers;

import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.function.Consumer;

public class MySQLDriver implements Driver {
    @Override
    public void setup() {

    }

    @Override
    public void loadPlayer(Player player, Consumer<ResultSet> callback) {

    }

    @Override
    public void loadPlayers(Consumer<ResultSet> callback) {

    }

    @Override
    public void savePlayer(CratePlayer player) {

    }

    @Override
    public void savePlayers(boolean async) {

    }
}
