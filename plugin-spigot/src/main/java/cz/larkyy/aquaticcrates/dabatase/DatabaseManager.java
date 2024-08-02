package cz.larkyy.aquaticcrates.dabatase;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.config.Config;
import cz.larkyy.aquaticcrates.dabatase.drivers.Driver;
import cz.larkyy.aquaticcrates.dabatase.drivers.MySQLDriver;
import cz.larkyy.aquaticcrates.dabatase.drivers.SQLiteDriver;
import cz.larkyy.aquaticcrates.player.CratePlayer;
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

    private final Config config = new Config(AquaticCrates.instance(),"database-settings.yml");

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
        CratePlayer cp = new CratePlayer(player);
        driver.loadPlayer(player, rs -> {
            try {
                while (rs.next()) {
                    String id = rs.getString("Identifier");
                    int amount = rs.getInt("Amount");
                    cp.addKeys(id,amount);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            AquaticCrates.getPlayerHandler().addPlayer(player.getUniqueId(),cp);
            callback.accept(cp);
        });

    }
    public void loadPlayers(Runnable callback) {
        driver.loadPlayers(rs -> {
            try {
                while (rs.next()) {
                    Player p = Bukkit.getPlayer(UUID.fromString(rs.getString("UniqueID")));
                    if (p == null) continue;
                    if (!p.isOnline()) continue;
                    CratePlayer cp = new CratePlayer(p);
                    String id = rs.getString("Identifier");
                    int amount = rs.getInt("Amount");
                    cp.addKeys(id,amount);
                    AquaticCrates.getPlayerHandler().addPlayer(p.getUniqueId(),cp);
                }
                callback.run();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void savePlayer(CratePlayer cratePlayer) {
        driver.savePlayer(cratePlayer,true);
    }

    public void savePlayers(boolean async) {
        driver.savePlayers(async);
    }

    public FileConfiguration getCfg() {
        return config.getConfiguration();
    }

}
