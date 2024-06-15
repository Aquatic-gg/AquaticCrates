package cz.larkyy.aquaticcrates.animation.task.impl;

import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.impl.CinematicAnimation;
import cz.larkyy.aquaticcrates.animation.task.Task;
import cz.larkyy.aquaticcrates.animation.task.TaskArgument;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CameraTeleportTask extends Task {

    public static final List<TaskArgument> ARGUMENTS = new ArrayList<>();
    static {
        ARGUMENTS.add(new TaskArgument("delay",0,false));
        ARGUMENTS.add(new TaskArgument("offset","0;0;0",true));
        ARGUMENTS.add(new TaskArgument("pitch",0,true));
        ARGUMENTS.add(new TaskArgument("yaw",0,true));
    }

    @Override
    public void run(Animation animation, Map<String,Object> arguments) {
        if (animation instanceof CinematicAnimation a) {
            a.teleportCamera(
                    readVector(arguments.get("offset").toString()),
                    Float.parseFloat(arguments.get("yaw").toString()),
                    Float.parseFloat(arguments.get("pitch").toString())
            );
        }
    }

    @Override
    public List<TaskArgument> getArgs() {
        return ARGUMENTS;
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
