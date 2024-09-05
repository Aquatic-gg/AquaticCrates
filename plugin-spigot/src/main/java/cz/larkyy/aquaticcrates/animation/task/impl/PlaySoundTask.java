package cz.larkyy.aquaticcrates.animation.task.impl;

import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.task.AnimationTask;
import gg.aquatic.aquaticseries.lib.util.argument.AquaticObjectArgument;
import gg.aquatic.aquaticseries.lib.util.argument.impl.PrimitiveObjectArgument;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholders;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class PlaySoundTask extends AnimationTask {
    @Override
    public void run(Animation animation, @NotNull Map<String, ?> arguments, @NotNull BiFunction<Animation, String, String> biFunction) {
        Location location;
        if (animation.getModel() == null) {
            location = animation.getPlayer().getLocation();
        } else {
            location = animation.getModel().getLocation().clone().add(readVector(arguments.get("offset").toString()));
        }

        animation.getPlayer().playSound(
                location,
                arguments.get("sound").toString().toLowerCase(),
                Float.parseFloat(arguments.get("volume").toString()),
                Float.parseFloat(arguments.get("pitch").toString())
        );
    }

    @NotNull
    @Override
    public List<AquaticObjectArgument<?>> arguments() {
        return List.of(
                new PrimitiveObjectArgument("sound","minecraft:block.note_block.bell",true),
                new PrimitiveObjectArgument("pitch",1d,false),
                new PrimitiveObjectArgument("volume",100f,false),
                new PrimitiveObjectArgument("offset","0;0;0",false)
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
