package cz.larkyy.aquaticcrates.camera;

import cz.larkyy.aquaticcrates.AquaticCrates;
import gg.aquatic.aquaticseries.lib.audience.WhitelistAudience;
import gg.aquatic.aquaticseries.lib.nms.NMSAdapter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Camera {

    private int id;
    private final Player player;
    private Location prevLocation;
    private GameMode prevMode;
    private CameraMovement cameraMovement;

    public Camera(Location location, Player player) {
        this.player = player;
        this.id = spawnEntity(location.clone().add(0, 1.8, 0));
    }

    private int spawnEntity(Location location) {
        var id = nmsHandler().spawnEntity(location,
                "armor_stand",
                new WhitelistAudience(new ArrayList<>() {{
                    add(player.getUniqueId());
                }}),
                e -> {
                    ArmorStand as = (ArmorStand) e;
                    as.setInvisible(true);
                    as.setMarker(true);
                    as.setPersistent(false);
                }
        );
        move(location);
        return id;
    }

    public Location location() {
        return nmsHandler().getEntity(id).getLocation();
    }

    public void teleport(Location location) {
        if (cameraMovement != null) {
            cameraMovement.stop();
        }
        Bukkit.getScheduler().runTask(AquaticCrates.instance(), () -> {
            nmsHandler().teleportEntity(id, location, new WhitelistAudience(new ArrayList<>() {{
                add(player.getUniqueId());
            }}));
        });
    }

    public void move(Location location) {
        Bukkit.getScheduler().runTask(AquaticCrates.instance(), () -> {
            nmsHandler().moveEntity(id, location, new WhitelistAudience(new ArrayList<>() {{
                add(player.getUniqueId());
            }}));
        });
    }

    public void setMovement(Vector offSet, int tickDuration, float yawOffset, float pitchOffset) {
        if (cameraMovement != null) {
            cameraMovement.stop();
        }

        Location l = location().clone().add(offSet);
        l.setYaw(l.getYaw() + yawOffset);
        l.setPitch(l.getPitch() + pitchOffset);

        cameraMovement = new CameraMovement(this, l, tickDuration);
        cameraMovement.start();
    }

    public void attachPlayer(Runnable runnable) {

        prevLocation = player.getPlayer().getLocation();
        prevMode = player.getGameMode();
        int delay = 0;
        if (!player.getLocation().getWorld().equals(location().getWorld())) {
            delay = 5;
        }
        player.teleport(location().clone().add(0, 2, 0));
        new BukkitRunnable() {
            @Override
            public void run() {
                id = spawnEntity(location());
                player.setGameMode(GameMode.SPECTATOR);
                nmsHandler().setPlayerInfoGamemode(GameMode.CREATIVE, player);
                nmsHandler().setGamemode(GameMode.SPECTATOR, player);
                nmsHandler().setSpectatorTarget(id, new WhitelistAudience(new ArrayList<>() {{
                    add(player.getUniqueId());
                }}));
                runnable.run();
            }
        }.runTaskLater(AquaticCrates.instance(), delay);
    }

    public void detachPlayer() {
        nmsHandler().setSpectatorTarget(0, new WhitelistAudience(new ArrayList<>() {{
            add(player.getUniqueId());
        }}));
        nmsHandler().setGamemode(prevMode, player);
        player.setGameMode(prevMode);
        player.teleport(prevLocation);
    }

    public void despawn() {
        if (cameraMovement != null) {
            cameraMovement.stop();
        }
        detachPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                nmsHandler().despawnEntity(List.of(id), new WhitelistAudience(new ArrayList<>() {{
                    add(player.getUniqueId());
                }}));
            }
        }.runTaskLaterAsynchronously(AquaticCrates.instance(), 1);
    }

    private NMSAdapter nmsHandler() {
        return AquaticCrates.getNmsHandler();
    }
}
