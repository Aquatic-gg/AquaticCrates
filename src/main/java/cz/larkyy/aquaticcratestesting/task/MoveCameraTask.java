package cz.larkyy.aquaticcratestesting.task;

import cz.larkyy.aquaticcratestesting.Animation;
import org.bukkit.util.Vector;

public class MoveCameraTask extends Task {

    private final Vector offset;
    private final int duration;

    public MoveCameraTask(int delay, Vector offSet, int duration) {
        super(delay);
        this.offset = offSet;
        this.duration = duration;
    }

    @Override
    public void run(Animation animation) {
        animation.getCamera().setMovement(offset,duration);
    }
}
