package cz.larkyy.aquaticcratestesting.animation.task;

import cz.larkyy.aquaticcratestesting.animation.Animation;

import java.util.List;
import java.util.Map;

public abstract class Task {

    public abstract void run(Animation animation, Map<String,Object> arguments);

    public abstract List<TaskArgument> getArgs();
}
