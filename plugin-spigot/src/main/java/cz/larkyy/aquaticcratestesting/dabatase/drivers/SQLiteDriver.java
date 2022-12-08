package cz.larkyy.aquaticcratestesting.dabatase.drivers;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.dabatase.DatabaseManager;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.function.Consumer;

public class SQLiteDriver implements Driver {

    private final File databaseFile = new File(AquaticCratesTesting.instance().getDataFolder(), "database.db");
    private Connection activeConnection;

    @Override
    public void setup() throws IOException, SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        AquaticCratesTesting.instance().getDataFolder().mkdirs();
        databaseFile.createNewFile();

        Connection connection = this.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(
                "CREATE TABLE IF NOT EXISTS aquaticcrates_keys (\n" +
                        "    id integer primary key,\n" +
                        "    UniqueID NVARCHAR(64) NOT NULL,\n" +
                        "    Identifier NVARCHAR(64) NOT NULL,\n" +
                        "    Amount INT NOT NULL\n" +
                        ");"
        );

        statement.close();
    }

    @Override
    public void loadPlayer(Player player, Consumer<ResultSet> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                try (PreparedStatement ps = connection.prepareStatement("" +
                        "SELECT Identifier, Amount\n" +
                        "FROM aquaticcrates_keys\n" +
                        "WHERE UniqueID = ?;"
                )) {
                    ps.setString(1,player.getUniqueId().toString());
                    callback.accept(ps.executeQuery());
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
                Connection connection = getConnection();
                try (PreparedStatement ps = connection.prepareStatement("" +
                        "SELECT UniqueID, Identifier, Amount\n" +
                        "FROM aquaticcrates_keys;"
                )) {
                    callback.accept(ps.executeQuery());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(AquaticCratesTesting.instance());
    }

    @Override
    public void savePlayer(CratePlayer player, boolean async) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                try(Connection connection = getConnection()) {
                    player.getVirtualKeys().forEach((id,i) -> {
                        try (PreparedStatement ps = connection.prepareStatement(
                                "SELECT id FROM aquaticcrates_keys WHERE UniqueID = ? AND Identifier = ?"
                        )) {
                            ps.setString(1,player.getPlayer().getUniqueId().toString());
                            ps.setString(2,id);

                            ResultSet rs = ps.executeQuery();
                            if (rs.next()) {
                                try(PreparedStatement ps2 = connection.prepareStatement(
                                        "UPDATE aquaticcrates_keys SET Amount = ? WHERE UniqueID = ? AND Identifier = ?"
                                )) {
                                    ps2.setInt(1,i);
                                    ps2.setString(2,player.getPlayer().getUniqueId().toString());
                                    ps2.setString(3,id);

                                    ps2.execute();
                                }
                            } else {
                                try(PreparedStatement ps2 = connection.prepareStatement(
                                        "INSERT OR REPLACE INTO aquaticcrates_keys (UniqueID, Identifier, Amount) VALUES (?, ?, ?);"
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
                    });
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        if (async) {
            runnable.runTaskAsynchronously(AquaticCratesTesting.instance());
        } else {
            runnable.run();
        }
    }

    @Override
    public void savePlayers(boolean async) {
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                try(Connection connection = getConnection()) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        CratePlayer cp = CratePlayer.get(p);
                        for (Map.Entry<String, Integer> entry : cp.getVirtualKeys().entrySet()) {
                            String id = entry.getKey();
                            Integer i = entry.getValue();
                            try (PreparedStatement ps = connection.prepareStatement(
                                    "SELECT id FROM aquaticcrates_keys WHERE UniqueID = ? AND Identifier = ?"
                            )) {
                                ps.setString(1, p.getPlayer().getUniqueId().toString());
                                ps.setString(2, id);

                                ResultSet rs = ps.executeQuery();
                                if (rs.next()) {
                                    try (PreparedStatement ps2 = connection.prepareStatement(
                                            "UPDATE aquaticcrates_keys SET Amount = ? WHERE UniqueID = ? AND Identifier = ?"
                                    )) {
                                        ps2.setInt(1, i);
                                        ps2.setString(2, p.getPlayer().getUniqueId().toString());
                                        ps2.setString(3, id);

                                        ps2.execute();
                                    }
                                } else {
                                    try (PreparedStatement ps2 = connection.prepareStatement(
                                            "INSERT OR REPLACE INTO aquaticcrates_keys (UniqueID, Identifier, Amount) VALUES (?, ?, ?);"
                                    )) {
                                        ps2.setString(1, p.getPlayer().getUniqueId().toString());
                                        ps2.setString(2, id);
                                        ps2.setInt(3, i);

                                        ps2.execute();
                                    }
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        if (async) {
            task.runTaskAsynchronously(AquaticCratesTesting.instance());
        } else {
            task.run();
        }
    }

    private Connection getConnection() {
        try {
            if (activeConnection != null && !activeConnection.isClosed()) return this.activeConnection;
            Class.forName("org.sqlite.SQLiteDataSource");
            this.activeConnection = DriverManager.getConnection("jdbc:sqlite:"+this.databaseFile.getPath());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return this.activeConnection;
    }
}
