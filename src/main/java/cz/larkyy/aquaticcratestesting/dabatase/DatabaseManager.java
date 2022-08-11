package cz.larkyy.aquaticcratestesting.dabatase;

import cz.larkyy.aquaticcratestesting.api.AquaticCratesAPI;
import cz.larkyy.aquaticcratestesting.dabatase.drivers.Driver;
import cz.larkyy.aquaticcratestesting.dabatase.drivers.MySQLDriver;
import cz.larkyy.aquaticcratestesting.dabatase.drivers.SQLiteDriver;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DatabaseManager {

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS aquaticcrates_keys (\n" +
            "    id integer primary key,\n" +
            "    UniqueID NVARCHAR(64) NOT NULL,\n" +
            "    Identifier NVARCHAR(64) NOT NULL,\n" +
            "    Amount INT NOT NULL\n" +
            ");";
    private final Map<String,Driver> availableDrivers = new HashMap<>(){
        {
            put("sqlite",new SQLiteDriver());
            put("mysql",new MySQLDriver());
        }
    };
    private final Driver driver;

    public DatabaseManager() throws SQLException, IOException, ClassNotFoundException {
        driver = availableDrivers.get("sqlite");
        driver.setup();
    }

    public void loadPlayer(Player player, Consumer<CratePlayer> callback) {

        driver.loadPlayer(player, rs -> {

            CratePlayer cp = new CratePlayer(player);
            try {
                while (rs.next()) {
                    Bukkit.broadcastMessage("Loading key");
                    String id = rs.getString("Identifier");
                    int amount = rs.getInt("Amount");
                    cp.addKeys(id,amount);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            AquaticCratesAPI.getPlayerHandler().addPlayer(player,cp);
            callback.accept(cp);
        });

    }
    public void loadPlayers() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            loadPlayer(p, cp -> {});
        });
    }

    public void savePlayer(CratePlayer cratePlayer) {
        driver.savePlayer(cratePlayer);
    }

    public void savePlayers(boolean async) {
        driver.savePlayers(async);
    }

}
