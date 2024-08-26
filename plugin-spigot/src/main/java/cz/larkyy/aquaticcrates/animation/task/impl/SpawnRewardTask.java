package cz.larkyy.aquaticcrates.animation.task.impl;

import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.task.AnimationTask;
import gg.aquatic.aquaticseries.lib.util.argument.AquaticObjectArgument;
import gg.aquatic.aquaticseries.lib.util.argument.impl.PrimitiveObjectArgument;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholders;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class SpawnRewardTask extends AnimationTask {
    @Override
    public void run(Animation animation, @NotNull Map<String, ?> arguments, @NotNull Placeholders placeholders) {
        animation.spawnReward(
                (Integer) arguments.get("rumblingLength"),
                (Integer) arguments.get("rumblingPeriod"),
                (Integer) arguments.get("length"),
                readVector(arguments.get("velocity").toString()),
                Boolean.parseBoolean(arguments.get("gravity").toString()),
                readVector(arguments.get("offset").toString()),
                Boolean.parseBoolean(arguments.get("ease-out").toString())
        );
    }

    @NotNull
    @Override
    public List<AquaticObjectArgument<?>> arguments() {
        return List.of(
                new PrimitiveObjectArgument("delay",0,false),
                new PrimitiveObjectArgument("length",10000,false),
                new PrimitiveObjectArgument("gravity",true,false),
                new PrimitiveObjectArgument("velocity","0;0;0",false),
                new PrimitiveObjectArgument("rumbling-length",0,false),
                new PrimitiveObjectArgument("rumbling-period",4,false),
                new PrimitiveObjectArgument("offset","0;0;0",false),
                new PrimitiveObjectArgument("ease-out",false,false)
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
