package cz.larkyy.aquaticcratestesting.camera;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class CameraMovement {


    private final Location previousLocation;
    private final Location destinationLocation;
    private final int duration;
    private final Camera camera;
    private BukkitRunnable runnable;
    private int i = 0;

    public CameraMovement(Camera camera, Location destinationLocation, int duration) {
        this.camera = camera;
        this.destinationLocation = destinationLocation.clone();
        this.duration = duration;

        this.previousLocation = camera.location().clone();
    }

    public Location getLocation(int tick) {

        Vector v1 = destinationLocation.toVector().subtract(previousLocation.toVector());
        v1.multiply((double) (1d/duration));
        v1.multiply(tick);

        Location location = previousLocation.clone().add(v1);

        float yaw = destinationLocation.getYaw() - previousLocation.getYaw();
        float pitch = destinationLocation.getPitch() - previousLocation.getPitch();

        yaw /= duration;
        yaw *= tick;
        pitch /= duration;
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
        runnable.runTaskTimer(AquaticCratesTesting.getPlugin(AquaticCratesTesting.class),0,1);
    }

    public void stop() {
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
        }
    }
}
