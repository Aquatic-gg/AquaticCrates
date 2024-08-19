package cz.larkyy.aquaticcrates.animation.task.impl;

import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.task.Task;
import cz.larkyy.aquaticcrates.animation.task.TaskArgument;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpawnRewardTask extends Task {

    public static final List<TaskArgument> ARGUMENTS = new ArrayList<>();
    static {
        ARGUMENTS.add(new TaskArgument("delay",0,false));
        ARGUMENTS.add(new TaskArgument("length",10000,false));
        ARGUMENTS.add(new TaskArgument("gravity",true,false));
        ARGUMENTS.add(new TaskArgument("velocity","0;0;0",false));
        ARGUMENTS.add(new TaskArgument("rumblingLength",0,false));
        ARGUMENTS.add(new TaskArgument("rumblingPeriod",4,false));
        ARGUMENTS.add(new TaskArgument("offset","0;0;0",false));
        ARGUMENTS.add(new TaskArgument("ease-out",false,false));
    }

    @Override
    public void run(Animation animation, Map<String,Object> arguments) {
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
