package cz.larkyy.aquaticcratestesting.animation.task.impl;

import cz.larkyy.aquaticcratestesting.animation.Animation;
import cz.larkyy.aquaticcratestesting.animation.task.Task;
import cz.larkyy.aquaticcratestesting.animation.task.TaskArgument;
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
    }

    public SpawnRewardTask(Map<String,Object> arguments) {
        super(arguments);
    }

    @Override
    public void run(Animation animation) {
        animation.spawnReward(
                (Integer) getArg("rumblingLength"),
                (Integer) getArg("rumblingPeriod"),
                (Integer) getArg("length"),
                readVector(getArg("velocity").toString()),
                Boolean.parseBoolean(getArg("gravity").toString()),
                readVector(getArg("offset").toString())
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
