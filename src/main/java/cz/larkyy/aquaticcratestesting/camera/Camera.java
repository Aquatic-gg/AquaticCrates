package cz.larkyy.aquaticcratestesting.camera;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;

public class Camera {

    private final ArmorStand armorStand;

    private Camera(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }

    public Location location() {
        return armorStand.getLocation();
    }

    public void teleport(Location location) {
        armorStand.teleport(location);
    }

    public void setMovement(Vector offSet, int tickDuration) {
        CameraMovement cameraMovement = new CameraMovement(this,location().clone().add(offSet),tickDuration);
        cameraMovement.start();
    }

    public void attachPlayer(Player player) {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        // event packet
        final PacketContainer eventPacket
                = manager.createPacket(PacketType.Play.Server.GAME_STATE_CHANGE);
        eventPacket.getGameStateIDs().write(0, 3);
        eventPacket.getFloat().write(0, 3f);
        try {
            manager.sendServerPacket(player, eventPacket);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // camera packet
        final PacketContainer cameraPacket
                = manager.createPacket(PacketType.Play.Server.CAMERA);
        cameraPacket.getEntityModifier(player.getWorld()).write(0, armorStand);
        try {
            manager.sendServerPacket(player, cameraPacket);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void detachPlayer(Player player) {
        if (player == null)
            return;

        final int gameModeId = player.getGameMode().getValue();
        try {
            final PacketContainer pkt = new PacketContainer(PacketType.Play.Server.GAME_STATE_CHANGE);
            pkt.getGameStateIDs().write(0, 3);
            pkt.getModifier().write(1, gameModeId);

            ProtocolLibrary.getProtocolManager().sendServerPacket(player, pkt);
        } catch (final Exception e) {
            throw new RuntimeException("Cannot send server packet.", e);
        }

        final PacketContainer pkt = new PacketContainer(PacketType.Play.Server.CAMERA);
        pkt.getModifier().write(0, player.getEntityId());
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, pkt);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException("Cannot send server packet.", e);
        }
    }

    public void despawn() {
        armorStand.remove();
    }

    public static Camera spawn(Location location) {
        final Location cameraLoc = location.clone()
                .add(0, 1.8, 0);

        // force chunk
        if (!cameraLoc.getChunk().isLoaded())
            cameraLoc.getChunk().load();

        final ArmorStand camera = (ArmorStand) cameraLoc.getWorld()
                .spawnEntity(cameraLoc.clone(), EntityType.ARMOR_STAND);
        {
            camera.setInvisible(true);
            camera.setMarker(true);
            camera.setPersistent(false);
        }

        return new Camera(camera);
    }

}
