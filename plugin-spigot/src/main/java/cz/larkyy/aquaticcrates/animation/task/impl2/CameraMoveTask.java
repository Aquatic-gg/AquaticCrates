package cz.larkyy.aquaticcrates.animation.task.impl2;

import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.impl.CinematicAnimation;
import cz.larkyy.aquaticcrates.animation.task.AnimationTask;
import gg.aquatic.aquaticseries.lib.action.AbstractAction;
import gg.aquatic.aquaticseries.lib.util.argument.AquaticObjectArgument;
import gg.aquatic.aquaticseries.lib.util.argument.impl.PrimitiveObjectArgument;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholders;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class CameraMoveTask extends AnimationTask {
    @Override
    public void run(Animation animation, @NotNull Map<String, ?> arguments, @NotNull Placeholders placeholders) {
        if (animation instanceof CinematicAnimation a) {
            a.moveCamera(
                    readVector(arguments.get("offset").toString()),
                    (int)arguments.get("duration"),
                    Float.parseFloat(arguments.get("rotate-head-yaw").toString()),
                    Float.parseFloat(arguments.get("rotate-head-pitch").toString())
            );
        }
    }

    @NotNull
    @Override
    public List<AquaticObjectArgument<?>> arguments() {
        return List.of(
                new PrimitiveObjectArgument("delay",0,false),
                new PrimitiveObjectArgument("duration",100,true),
                new PrimitiveObjectArgument("offset","0;0;0",true),
                new PrimitiveObjectArgument("rotate-head-yaw",0,false),
                new PrimitiveObjectArgument("rotate-head-pitch",0,false)
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
