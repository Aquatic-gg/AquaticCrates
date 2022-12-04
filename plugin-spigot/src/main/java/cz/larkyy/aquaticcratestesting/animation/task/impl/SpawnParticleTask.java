package cz.larkyy.aquaticcratestesting.animation.task.impl;

import cz.larkyy.aquaticcratestesting.animation.Animation;
import cz.larkyy.aquaticcratestesting.animation.task.Task;
import cz.larkyy.aquaticcratestesting.animation.task.TaskArgument;
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
        ARGUMENTS.add(new TaskArgument("particle","0;0;0",false));
        ARGUMENTS.add(new TaskArgument("count",1,false));
    }

    public SpawnParticleTask(Map<String, Object> arguments) {
        super(arguments);
    }

    @Override
    public void run(Animation animation) {
        animation.getModel().getLocation().getWorld().spawnParticle(
                Particle.valueOf(getArg("particle").toString()),
                animation.getModel().getLocation().add(readVector(getArg("offset").toString())),
                Integer.parseInt(getArg("count").toString()),
                Integer.parseInt(getArg("offsetX").toString()),
                Integer.parseInt(getArg("offsetY").toString()),
                Integer.parseInt(getArg("offsetZ").toString())
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
