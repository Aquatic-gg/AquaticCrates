package cz.larkyy.aquaticcratestesting.dabatase;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.api.AquaticCratesAPI;
import cz.larkyy.aquaticcratestesting.config.Config;
import cz.larkyy.aquaticcratestesting.dabatase.drivers.Driver;
import cz.larkyy.aquaticcratestesting.dabatase.drivers.MySQLDriver;
import cz.larkyy.aquaticcratestesting.dabatase.drivers.SQLiteDriver;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class DatabaseManager {

    private final Config config = new Config(AquaticCratesTesting.instance(),"database-settings.yml");

    private final Map<String,Driver> availableDrivers = new HashMap<>(){
        {
            put("sqlite",new SQLiteDriver());
            put("mysql",new MySQLDriver());
        }
    };
    private final Driver driver;

    public DatabaseManager() {
        config.load();
        driver = availableDrivers.get(config.getConfiguration().getString("type","sqlite").toLowerCase());
    }

    public void setup() throws SQLException, IOException, ClassNotFoundException {
        driver.setup();
    }

    public void loadPlayer(Player player, Consumer<CratePlayer> callback) {

        CratePlayer cp = CratePlayer.get(player);
        driver.loadPlayer(player, rs -> {
            try {
                while (rs.next()) {
                    String id = rs.getString("Identifier");
                    int amount = rs.getInt("Amount");
                    cp.addKeys(id,amount);
                    Bukkit.broadcastMessage("Adding "+id+": "+amount);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            callback.accept(cp);
        });

    }
    public void loadPlayers() {
        driver.loadPlayers(rs -> {
            try {
                while (rs.next()) {
                    Player p = Bukkit.getPlayer(UUID.fromString(rs.getString("UniqueID")));
                    CratePlayer cp = CratePlayer.get(p);
                    String id = rs.getString("Identifier");
                    int amount = rs.getInt("Amount");
                    cp.addKeys(id,amount);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void savePlayer(CratePlayer cratePlayer) {
        driver.savePlayer(cratePlayer);
    }

    public void savePlayers(boolean async) {
        driver.savePlayers(async);
    }

    public FileConfiguration getCfg() {
        return config.getConfiguration();
    }

}
