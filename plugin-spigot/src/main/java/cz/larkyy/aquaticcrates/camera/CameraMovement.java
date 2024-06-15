package cz.larkyy.aquaticcrates.camera;

import cz.larkyy.aquaticcrates.AquaticCrates;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class CameraMovement {


    private final Location previousLocation;
    private final Location destinationLocation;
    private final int duration;
    private final Camera camera;
    private BukkitRunnable runnable;
    private final float yawOffset;
    private final float pitchOffset;
    private final Vector offset;
    private int i = 0;

    public CameraMovement(Camera camera, Location destinationLocation, int duration) {
        this.camera = camera;
        this.destinationLocation = destinationLocation.clone();
        this.duration = duration;

        this.previousLocation = camera.location().clone();
        offset = destinationLocation.toVector().subtract(previousLocation.toVector());
        offset.multiply((double) (1d/duration));

        yawOffset = (destinationLocation.getYaw() - previousLocation.getYaw()) / duration;
        pitchOffset = (destinationLocation.getPitch() - previousLocation.getPitch()) / duration;
    }

    public Location getLocation(int tick) {

        Vector v1 = offset.clone();
        v1.multiply(tick);

        Location location = previousLocation.clone().add(v1);

        float yaw = yawOffset;
        float pitch = pitchOffset;
        yaw *= tick;
        pitch *= tick;

        location.setYaw(previousLocation.getYaw()+yaw);
        location.setPitch(previousLocation.getPitch()+pitch);

        return location;
    }

    public void move(int tick) {
        Location location = getLocation(tick);

        camera.move(location);
    }

    public void start() {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (i > duration) {
                    cancel();
                }
                move(i);
                i++;
            }
        };
        runnable.runTaskTimerAsynchronously(AquaticCrates.getPlugin(AquaticCrates.class),0,1);
    }

    public void stop() {
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
        }
    }
}
