package cz.larkyy.aquaticcratestesting.animation.task.impl;

import cz.larkyy.aquaticcratestesting.animation.Animation;
import cz.larkyy.aquaticcratestesting.animation.task.Task;
import cz.larkyy.aquaticcratestesting.animation.task.TaskArgument;
import xyz.larkyy.colorutils.Colors;

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

    public SendTitleTask(Map<String, Object> arguments) {
        super(arguments);
    }

    @Override
    public void run(Animation animation) {
        animation.getPlayer().sendTitle(
                Colors.format(getArg("title").toString()),
                Colors.format(getArg("subtitle").toString()),
                (int)getArg("in"),
                (int)getArg("stay"),
                (int)getArg("out")
                );
    }
}
