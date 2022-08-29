package cz.larkyy.aquaticcratestesting.animation.task.impl;

import cz.larkyy.aquaticcratestesting.animation.Animation;
import cz.larkyy.aquaticcratestesting.animation.task.Task;
import cz.larkyy.aquaticcratestesting.animation.task.TaskArgument;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlaySoundTask extends Task {

    public static final List<TaskArgument> ARGUMENTS = new ArrayList<>();
    static {
        ARGUMENTS.add(new TaskArgument("delay",0,false));
        ARGUMENTS.add(new TaskArgument("sound","minecraft:block.note_block.bell",true));
        ARGUMENTS.add(new TaskArgument("pitch",1d,false));
    }

    public PlaySoundTask(Map<String, Object> arguments) {
        super(arguments);
    }

    @Override
    public void run(Animation animation) {
        animation.getPlayer().playSound(
                animation.getPlayer().getLocation(),
                getArg("sound").toString().toLowerCase(),
                100f,
                Float.parseFloat(getArg("pitch").toString())
        );
    }
}
