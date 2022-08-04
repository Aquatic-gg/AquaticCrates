package cz.larkyy.aquaticcratestesting;

import cz.larkyy.aquaticcratestesting.camera.Camera;
import cz.larkyy.aquaticcratestesting.task.Task;
import cz.larkyy.aquaticcratestesting.task.TaskHandler;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Animation extends BukkitRunnable {

    private final Player player;
    private final PlayerEmote playerEmote;
    private final ArmorStand emoteEntity;
    private final Model model;
    private final Camera camera;
    private final Location previousLocation;
    private final TaskHandler taskHandler;
    private int i = 0;

    private Animation(Model model, Camera camera, PlayerEmote playerEmote, List<Task> tasks, Player player) {
        this.model = model;
        this.camera = camera;
        this.player = player;
        this.playerEmote = playerEmote;
        this.previousLocation = player.getLocation().clone();
        this.emoteEntity = spawnEmoteEntity(camera.location().clone());
        this.taskHandler = new TaskHandler(this,tasks);
    }

    private ArmorStand spawnEmoteEntity(Location location) {
        final Location cameraLoc = location.clone()
                .add(0, -1.8, 0);

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
        return camera;
    }

    public void start() {
        if (i > 0) {
            return;
        }

        Utils.playScreenEffect(player,15,5);

        Animation instance = this;

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setInvisible(true);
                player.teleport(camera.location());

                camera.attachPlayer(player);
                model.playAnimation("open");
                playerEmote.play(player,emoteEntity);
                instance.runTaskTimer(AquaticCratesTesting.getPlugin(AquaticCratesTesting.class),0,1);

            }
        }.runTaskLater(AquaticCratesTesting.getPlugin(AquaticCratesTesting.class),15);
    }

    public void end() {
        cancel();

        camera.detachPlayer(player);
        camera.despawn();

        model.despawn();

        //Utils.playScreenEffect(player,5,15);
        player.setInvisible(false);
        player.teleport(previousLocation);
    }

    @Override
    public void run() {
        taskHandler.playTask(i);

        if (i > 400) {
            end();
            return;
        }
        i++;
    }

    public static Animation create(Model model, Camera camera, PlayerEmote playerEmote, List<Task> tasks, Player player) {
        Animation animation = new Animation(model,camera,playerEmote,tasks,player);
        animation.start();
        return animation;
    }

    public ArmorStand getEmoteEntity() {
        return emoteEntity;
    }

    public Camera getCamera() {
        return camera;
    }

    public Location getPreviousLocation() {
        return previousLocation;
    }

    public Model getModel() {
        return model;
    }

    public Player getPlayer() {
        return player;
    }
}
