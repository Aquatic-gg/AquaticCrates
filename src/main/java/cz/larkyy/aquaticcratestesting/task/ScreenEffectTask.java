package cz.larkyy.aquaticcratestesting.task;

import cz.larkyy.aquaticcratestesting.Animation;
import cz.larkyy.aquaticcratestesting.Utils;

public class ScreenEffectTask extends Task {

    private final int in;
    private final int out;

    public ScreenEffectTask(int delay, int in, int out) {
        super(delay);
        this.in = in;
        this.out = out;
    }

    @Override
    public void run(Animation animation) {
        Utils.playScreenEffect(animation.getPlayer(),in,out);
    }
}
