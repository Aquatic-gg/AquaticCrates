package cz.larkyy.aquaticcratestesting.animation.task;

import cz.larkyy.aquaticcratestesting.animation.Animation;

import java.util.Map;

public abstract class Task {
    private final Map<String,Object> arguments;

    public Task(Map<String,Object> arguments) {
        this.arguments = arguments;
    }

    public int getDelay() {
        return (int) arguments.get("delay");
    }

    public Object getArg(String id) {
        return arguments.get(id);
    }

    public abstract void run(Animation animation);
}
