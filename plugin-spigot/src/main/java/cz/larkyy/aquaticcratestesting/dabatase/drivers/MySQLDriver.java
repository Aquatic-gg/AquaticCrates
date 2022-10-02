package cz.larkyy.aquaticcratestesting.dabatase.drivers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.Map;
import java.util.function.Consumer;

public class MySQLDriver implements Driver {


    private HikariConfig config;
    private HikariDataSource dataSource;

    private String keysTableName;

    /**
     * Setup MySQL connection to server.
     */
    @Override
    public void setup() throws SQLException {
        this.keysTableName = String.format(
                "%saquaticcrates_keys", getCfg().getString("prefix")
        );

        this.config = new HikariConfig();
        // Construct configuration
        {
            String ip = getCfg().getString("ip");
            String port = getCfg().getString("port");
            String name = getCfg().getString("name");
            String usr = getCfg().getString("user");
            String pwd = getCfg().getString("password");

            config.setJdbcUrl(String.format(
                    "jdbc:mysql://%s:%s/%s", ip, port, name
            ));

            config.setMaximumPoolSize(10); // a.k.a. max connections to server
            config.setPoolName("AquaticCrates DB pool");

            config.setUsername(usr);
            config.setPassword(pwd);
        }

        // Construct data source
        {
            this.dataSource = new HikariDataSource(this.config);
        }


        try(final var connection = this.getConnection();
            final var statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS "+keysTableName+" (\n" +
                            "    id int NOT NULL AUTO_INCREMENT,\n" +
                            "    UniqueID NVARCHAR(64) NOT NULL,\n" +
                            "    Identifier NVARCHAR(64) NOT NULL,\n" +
                            "    Amount INT NOT NULL,\n" +
                            "    PRIMARY KEY (id)\n" +
                            ");"
            );
        }
    }

    /**
     * @return Connection to the MySQL server.
     */
    private Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    @Override
    public void loadPlayer(Player player, Consumer<ResultSet> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try (Connection connection = getConnection()) {
                    try (PreparedStatement ps = connection.prepareStatement("" +
                            "SELECT Identifier, Amount\n" +
                            "FROM " + keysTableName + "\n" +
                            "WHERE UniqueID = ?;"
                    )) {
                        ps.setString(1, player.getUniqueId().toString());
                        callback.accept(ps.executeQuery());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(AquaticCratesTesting.instance());
    }

    @Override
    public void loadPlayers(Consumer<ResultSet> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try (Connection connection = getConnection()) {
                    try (PreparedStatement ps = connection.prepareStatement("" +
                            "SELECT UniqueID, Identifier, Amount\n" +
                            "FROM " + keysTableName + ";"
                    )) {
                        callback.accept(ps.executeQuery());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(AquaticCratesTesting.instance());
    }

    @Override
    public void savePlayer(CratePlayer player) {
        Player p = player.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                try(Connection connection = getConnection()) {
                    for (Map.Entry<String, Integer> entry : player.getVirtualKeys().entrySet()) {
                        String id = entry.getKey();
                        Integer i = entry.getValue();
                        try (PreparedStatement ps = connection.prepareStatement(
                                "SELECT id FROM "+keysTableName+" WHERE UniqueID = ? AND Identifier = ?"
                        )) {
                            ps.setString(1,player.getPlayer().getUniqueId().toString());
                            ps.setString(2,id);

                            ResultSet rs = ps.executeQuery();
                            if (rs.next()) {
                                try(PreparedStatement ps2 = connection.prepareStatement(
                                        "UPDATE "+keysTableName+" SET Amount = ? WHERE UniqueID = ? AND Identifier = ?"
                                )) {
                                    ps2.setInt(1,i);
                                    ps2.setString(2,player.getPlayer().getUniqueId().toString());
                                    ps2.setString(3,id);

                                    ps2.execute();
                                }
                            } else {
                                try(PreparedStatement ps2 = connection.prepareStatement(
                                        "REPLACE INTO "+keysTableName+" (UniqueID, Identifier, Amount) VALUES (?, ?, ?);"
                                )) {
                                    ps2.setString(1,player.getPlayer().getUniqueId().toString());
                                    ps2.setString(2,id);
                                    ps2.setInt(3,i);

                                    ps2.execute();
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(AquaticCratesTesting.instance());
    }

    @Override
    public void savePlayers(boolean async) {
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    CratePlayer cp = CratePlayer.get(p);
                    savePlayer(cp);
                }
            }
        };
        if (async) {
            task.runTaskAsynchronously(AquaticCratesTesting.instance());
        } else {
            task.run();
        }
    }

    private FileConfiguration getCfg() {
        return AquaticCratesTesting.getDatabaseManager().getCfg();
    }
}