package cz.larkyy.aquaticcratestesting;

import cz.larkyy.aquaticcratestesting.camera.Camera;
import cz.larkyy.aquaticcratestesting.task.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 1) {
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "play" -> {

                if (!(sender instanceof Player player)) {
                    return false;
                }

                final Location modelLocation = new Location(Bukkit.getWorld("world"),951.5,73,710.5,180,0);
                Model model = Model.spawn("crate5",modelLocation);

                final Location cameraLocation = new Location(Bukkit.getWorld("world"),951.5,73,707.5,0,14);
                Camera camera = Camera.spawn(cameraLocation);

                PlayerEmote playerEmote = new PlayerEmote("open","openemote","open");

                List<Task> tasks = new ArrayList<>();
                tasks.add(new TeleportCameraTask(0,new Vector(-1,0,2),-106,24));
                tasks.add(new MoveCameraTask(0,new Vector(-1.5,2,2),213));
                tasks.add(new ScreenEffectTask(211,5,5));
                tasks.add(new TeleportCameraTask(216,new Vector(4,-1.5,-5.5),15,14));
                tasks.add(new MoveCameraTask(216,new Vector(-0.75,-0.5,2),100));
                tasks.add(new ScreenEffectTask(385,15,5));

                Animation.create(model,camera,playerEmote,tasks,player);
            }
        }

        return false;
    }
}
