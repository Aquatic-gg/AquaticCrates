package cz.larkyy.aquaticcrates.animation.task.impl;

import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.task.AnimationTask;
import gg.aquatic.aquaticseries.lib.util.argument.AquaticObjectArgument;
import gg.aquatic.aquaticseries.lib.util.argument.impl.PrimitiveObjectArgument;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholders;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class SpawnParticleTask extends AnimationTask {
    @Override
    public void run(Animation animation, @NotNull Map<String, ?> arguments, @NotNull Placeholders placeholders) {
        animation.getPlayer().spawnParticle(
                Particle.valueOf(arguments.get("particle").toString()),
                animation.getModel().getLocation().clone().add(readVector(arguments.get("offset").toString())),
                Integer.parseInt(arguments.get("count").toString()),
                Double.parseDouble(arguments.get("offset-x").toString()),
                Double.parseDouble(arguments.get("offset-y").toString()),
                Double.parseDouble(arguments.get("offset-z").toString()),
                1
        );
    }

    @NotNull
    @Override
    public List<AquaticObjectArgument<?>> arguments() {
        return List.of(
                new PrimitiveObjectArgument("offset","0;0;0",false),
                new PrimitiveObjectArgument("offset-x",0,false),
                new PrimitiveObjectArgument("offset-y",0,false),
                new PrimitiveObjectArgument("offset-z",0,false),
                new PrimitiveObjectArgument("particle","VILLAGER_HAPPY",true),
                new PrimitiveObjectArgument("count",1,false)
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
