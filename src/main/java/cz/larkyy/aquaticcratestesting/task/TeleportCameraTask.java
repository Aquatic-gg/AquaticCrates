package cz.larkyy.aquaticcratestesting.task;

import cz.larkyy.aquaticcratestesting.Animation;
import cz.larkyy.aquaticcratestesting.camera.Camera;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class TeleportCameraTask extends Task {

    private final Vector offSet;
    private final float yaw;
    private final float pitch;

    public TeleportCameraTask(int delay, Vector offSet, float yaw, float pitch) {
        super(delay);
        this.offSet = offSet;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void run(Animation animation) {
        Camera camera = animation.getCamera();

        Location loc = camera.location().clone().add(offSet);
        loc.setYaw(yaw);
        loc.setPitch(pitch);
        camera.teleport(loc);
    }
}
