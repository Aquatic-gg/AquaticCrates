package cz.larkyy.aquaticcratestesting.animation.task.impl;

import cz.larkyy.aquaticcratestesting.animation.Animation;
import cz.larkyy.aquaticcratestesting.animation.task.Task;
import cz.larkyy.aquaticcratestesting.animation.task.TaskArgument;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlaySoundTask extends Task {

    public static final List<TaskArgument> ARGUMENTS = new ArrayList<>();
    static {
        ARGUMENTS.add(new TaskArgument("delay",0,false));
        ARGUMENTS.add(new TaskArgument("sound","minecraft:block.note_block.bell",true));
        ARGUMENTS.add(new TaskArgument("pitch",1d,false));
        ARGUMENTS.add(new TaskArgument("volume",100f,false));
        ARGUMENTS.add(new TaskArgument("offset","0;0;0",false));
    }

    public PlaySoundTask(Map<String, Object> arguments) {
        super(arguments);
    }

    @Override
    public void run(Animation animation) {
        Location location;
        if (animation.getModel() == null) {
            location = animation.getPlayer().getLocation();
        } else {
            location = animation.getModel().getLocation().clone().add(readVector(getArg("offset").toString()));
        }

        animation.getPlayer().playSound(
                location,
                getArg("sound").toString().toLowerCase(),
                Float.parseFloat(getArg("volume").toString()),
                Float.parseFloat(getArg("pitch").toString())
        );
    }

    private Vector readVector(String str) {
        String[] strs = str.split(";");
        if (strs.length < 3) {
            return new Vector();
        }
        return new Vector(
                Double.parseDouble(strs[0]),
                Double.parseDouble(strs[1]),
                Double.parseDouble(strs[2])
        );
    }
}
