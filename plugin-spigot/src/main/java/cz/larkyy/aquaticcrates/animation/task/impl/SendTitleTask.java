package cz.larkyy.aquaticcrates.animation.task.impl;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.task.Task;
import cz.larkyy.aquaticcrates.animation.task.TaskArgument;
import gg.aquatic.aquaticseries.lib.StringExtKt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SendTitleTask extends Task {

    public static final List<TaskArgument> ARGUMENTS = new ArrayList<>();
    static {
        ARGUMENTS.add(new TaskArgument("delay",0,false));
        ARGUMENTS.add(new TaskArgument("in",0,false));
        ARGUMENTS.add(new TaskArgument("out",0,false));
        ARGUMENTS.add(new TaskArgument("stay",0,false));
        ARGUMENTS.add(new TaskArgument("title","",false));
        ARGUMENTS.add(new TaskArgument("subtitle","",false));
    }

    @Override
    public void run(Animation animation, Map<String,Object> arguments) {
        AquaticCrates.aquaticSeriesLib.getAdapter().getTitleAdapter().send(
                animation.getPlayer(),
                StringExtKt.toAquatic(arguments.get("title").toString()),
                StringExtKt.toAquatic(arguments.get("subtitle").toString()),
                (int)arguments.get("in"),
                (int)arguments.get("stay"),
                (int)arguments.get("out")
        );
    }

    @Override
    public List<TaskArgument> getArgs() {
        return ARGUMENTS;
    }
}
