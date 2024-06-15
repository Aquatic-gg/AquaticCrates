package cz.larkyy.aquaticcrates.animation.task;

import cz.larkyy.aquaticcrates.animation.Animation;

import java.util.List;
import java.util.Map;

public abstract class Task {

    public abstract void run(Animation animation, Map<String,Object> arguments);

    public abstract List<TaskArgument> getArgs();
}
