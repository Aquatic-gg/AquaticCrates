package cz.larkyy.aquaticcratestesting.animation.task.impl;

import cz.larkyy.aquaticcratestesting.animation.Animation;
import cz.larkyy.aquaticcratestesting.animation.task.Task;
import cz.larkyy.aquaticcratestesting.animation.task.TaskArgument;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandTask extends Task {

    public static final List<TaskArgument> ARGUMENTS = new ArrayList<>();
    static {
        ARGUMENTS.add(new TaskArgument("delay",0,false));
        ARGUMENTS.add(new TaskArgument("command",null,false));
    }

    @Override
    public void run(Animation animation, Map<String,Object> arguments) {
        if (arguments.get("command") == null) return;
        String command = (String) arguments.get("command");
        if (command == null) return;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command.replace("%player%",animation.getPlayer().getName()));
    }

    @Override
    public List<TaskArgument> getArgs() {
        return ARGUMENTS;
    }
}
