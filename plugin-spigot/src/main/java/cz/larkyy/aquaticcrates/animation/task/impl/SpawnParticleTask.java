package cz.larkyy.aquaticcrates.animation.task.impl;

import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.task.Task;
import cz.larkyy.aquaticcrates.animation.task.TaskArgument;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpawnParticleTask extends Task {

    public static final List<TaskArgument> ARGUMENTS = new ArrayList<>();
    static {
        ARGUMENTS.add(new TaskArgument("delay",0,false));
        ARGUMENTS.add(new TaskArgument("offset","0;0;0",false));
        ARGUMENTS.add(new TaskArgument("offsetX",0,false));
        ARGUMENTS.add(new TaskArgument("offsetY",0,false));
        ARGUMENTS.add(new TaskArgument("offsetZ",0,false));
        ARGUMENTS.add(new TaskArgument("particle","VILLAGER_ANGRY",true));
        ARGUMENTS.add(new TaskArgument("count",1,false));
    }

    @Override
    public void run(Animation animation, Map<String,Object> arguments) {
        animation.getPlayer().spawnParticle(
                Particle.valueOf(arguments.get("particle").toString()),
                animation.getModel().getLocation().clone().add(readVector(arguments.get("offset").toString())),
                Integer.parseInt(arguments.get("count").toString()),
                Double.parseDouble(arguments.get("offsetX").toString()),
                Double.parseDouble(arguments.get("offsetY").toString()),
                Double.parseDouble(arguments.get("offsetZ").toString()),
                1
        );
    }

    @Override
    public List<TaskArgument> getArgs() {
        return ARGUMENTS;
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
