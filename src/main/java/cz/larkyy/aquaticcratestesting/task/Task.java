package cz.larkyy.aquaticcratestesting.task;

import cz.larkyy.aquaticcratestesting.Animation;

public abstract class Task {

    private final int delay;

    public Task(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public abstract void run(Animation animation);
}
