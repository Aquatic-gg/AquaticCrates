package cz.larkyy.aquaticcrates.animation.task;

import cz.larkyy.aquaticcrates.animation.Animation;

import java.util.Map;

public class ConfiguredTask {

    private final Task task;
    private final Map<String,Object> values;
    private final int delay;

    public ConfiguredTask(Task task, Map<String,Object> values) {
        this.task = task;
        this.values = values;
        if (values.containsKey("delay")) {
            this.delay = Integer.parseInt(values.get("delay").toString());
        } else {
            this.delay = 0;
        }
    }

    public void run(Animation animation) {
        task.run(animation,values);
    }

    public int getDelay() {
        return delay;
    }
}
