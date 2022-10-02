package cz.larkyy.aquaticcratestesting.dabatase.drivers;

import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public interface Driver {

    void setup() throws IOException, SQLException, ClassNotFoundException;

    void loadPlayer(Player player, Consumer<ResultSet> callback);

    void loadPlayers(Consumer<ResultSet> callback);

    void savePlayer(CratePlayer player, boolean async);

    void savePlayers(boolean async);

}
