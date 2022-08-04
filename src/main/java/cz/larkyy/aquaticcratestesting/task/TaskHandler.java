package cz.larkyy.aquaticcratestesting.task;

import cz.larkyy.aquaticcratestesting.Animation;

import java.util.ArrayList;
import java.util.List;

public class TaskHandler {

    private final List<Task> tasks;
    private final Animation animation;

    public TaskHandler(Animation animation) {
        this.animation = animation;
        tasks = new ArrayList<>();
    }

    public TaskHandler(Animation animation, List<Task> tasks) {
        this.animation = animation;
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void addAllTasks(List<Task> tasks) {
        this.tasks.addAll(tasks);
    }

    public void playTask(int delay) {
        tasks.forEach(task -> {
            if (delay == task.getDelay()) {
                task.run(animation);
            }
        });
    }
}
