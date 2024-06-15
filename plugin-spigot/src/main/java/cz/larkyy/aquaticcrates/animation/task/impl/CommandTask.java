package cz.larkyy.aquaticcrates.animation.task.impl;

import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.task.Task;
import cz.larkyy.aquaticcrates.animation.task.TaskArgument;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandTask extends Task {

    public static final List<TaskArgument> ARGUMENTS = new ArrayList<>();
    static {
        ARGUMENTS.add(new TaskArgument("delay",0,false));
        ARGUMENTS.add(new TaskArgument("command",null,false));
        ARGUMENTS.add(new TaskArgument("commands",new ArrayList<>(),false));
    }

    @Override
    public void run(Animation animation, Map<String,Object> arguments) {
        if (arguments.get("command") == null) return;
        String command = (String) arguments.get("command");
        var commands = (List<String>) arguments.get("commands");
        if (command != null) {
            commands.add(command);
        }
        commands.forEach(cmd -> {
            if (cmd != null) Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd.replace("%player%",animation.getPlayer().getName()));
        });
    }

    @Override
    public List<TaskArgument> getArgs() {
        return ARGUMENTS;
    }
}
