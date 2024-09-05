package cz.larkyy.aquaticcrates.animation.task.impl;

import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.impl.CinematicAnimation;
import cz.larkyy.aquaticcrates.animation.task.AnimationTask;
import gg.aquatic.aquaticseries.lib.util.argument.AquaticObjectArgument;
import gg.aquatic.aquaticseries.lib.util.argument.impl.PrimitiveObjectArgument;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholders;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class CameraTeleportTask extends AnimationTask {
    @Override
    public void run(Animation animation, @NotNull Map<String, ?> arguments, @NotNull BiFunction<Animation, String, String> biFunction) {
        if (animation instanceof CinematicAnimation a) {
            a.teleportCamera(
                    readVector(arguments.get("offset").toString()),
                    Float.parseFloat(arguments.get("yaw").toString()),
                    Float.parseFloat(arguments.get("pitch").toString())
            );
        }
    }

    @NotNull
    @Override
    public List<AquaticObjectArgument<?>> arguments() {
        return List.of(
                new PrimitiveObjectArgument("offset","0;0;0", true),
                new PrimitiveObjectArgument("pitch",0,true),
                new PrimitiveObjectArgument("yaw",0,true)
        );
    }

    private Vector readVector(String str) {
        String[] strs = str.split(";");
        if (strs.length < 3) {
            return new Vector();
        }
        return new Vector(
                Double.parseDouble(strs[0]),
                Double.parseDouble(strs[1]),
                Double.parseDouble(strs[2])
        );
    }
}
