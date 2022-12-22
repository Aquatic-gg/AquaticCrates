package cz.larkyy.aquaticcratestesting.animation.task.impl;

import cz.larkyy.aquaticcratestesting.animation.Animation;
import cz.larkyy.aquaticcratestesting.animation.impl.CinematicAnimation;
import cz.larkyy.aquaticcratestesting.animation.task.Task;
import cz.larkyy.aquaticcratestesting.animation.task.TaskArgument;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CameraMoveTask extends Task {

    public static final List<TaskArgument> ARGUMENTS = new ArrayList<>();
    static {
        ARGUMENTS.add(new TaskArgument("delay",0,false));
        ARGUMENTS.add(new TaskArgument("duration",100,true));
        ARGUMENTS.add(new TaskArgument("offset","0;0;0",true));
        ARGUMENTS.add(new TaskArgument("rotate-head-yaw",0,false));
        ARGUMENTS.add(new TaskArgument("rotate-head-pitch",0,false));
    }

    @Override
    public void run(Animation animation, Map<String,Object> arguments) {
        if (animation instanceof CinematicAnimation a) {
            a.moveCamera(
                    readVector(arguments.get("offset").toString()),
                    (int)arguments.get("duration"),
                    Float.parseFloat(arguments.get("rotate-head-yaw").toString()),
                    Float.parseFloat(arguments.get("rotate-head-pitch").toString())
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
