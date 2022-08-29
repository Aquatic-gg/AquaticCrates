package cz.larkyy.aquaticcratestesting.camera;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.nms.NMSHandler;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class Camera {

    private final int id;
    private final Player player;
    private Location prevLocation;
    private GameMode prevMode;
    private CameraMovement cameraMovement;

    public Camera(Location location, Player player) {
        this.player = player;
        this.id = spawnEntity(location.clone().add(0,1.8,0));
    }

    private int spawnEntity(Location location) {
        return AquaticCratesTesting.getNmsHandler().spawnEntity(location,
                e -> {
                    ArmorStand as = (ArmorStand) e;
                    as.setInvisible(true);
                    as.setMarker(true);
                    as.setPersistent(false);
                },
                Arrays.asList(player),
                "armor_stand");
    }

    public Location location() {
        return nmsHandler().getEntity(id).getLocation();
    }

    public void teleport(Location location) {
        if (cameraMovement != null) {
            cameraMovement.stop();
        }
        nmsHandler().teleportEntity(id,location);
    }

    public void move(Location location) {
        nmsHandler().moveEntity(id,location);
    }

    public void setMovement(Vector offSet, int tickDuration, float yawOffset, float pitchOffset) {
        if (cameraMovement != null) {
            cameraMovement.stop();
        }

        Location l = location().clone().add(offSet);
        l.setYaw(l.getYaw()+yawOffset);
        l.setPitch(l.getPitch()+pitchOffset);

        cameraMovement = new CameraMovement(this,l,tickDuration);
        cameraMovement.start();
    }

    public void attachPlayer() {

        prevLocation = player.getPlayer().getLocation();
        prevMode = player.getGameMode();
        player.teleport(location().clone().add(0,2,0));
        player.setGameMode(GameMode.SPECTATOR);
        nmsHandler().setPlayerInfo("UPDATE_GAME_MODE", player.getPlayer(),"CREATIVE");
        nmsHandler().changeGamemode(player, GameMode.SPECTATOR);
        nmsHandler().setCamera(id,player);
    }

    public void detachPlayer() {
        nmsHandler().setCamera(0,player);
        nmsHandler().changeGamemode(player,prevMode);
        player.setGameMode(prevMode);
        player.teleport(prevLocation);
    }

    public void despawn() {
        if (cameraMovement != null) {
            cameraMovement.stop();
        }
        detachPlayer();
        nmsHandler().despawnEntity(Arrays.asList(id),Arrays.asList(player));
    }

    private NMSHandler nmsHandler() {
        return AquaticCratesTesting.getNmsHandler();
    }
}
