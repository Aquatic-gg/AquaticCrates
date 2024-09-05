package cz.larkyy.aquaticcrates.animation.task.impl;

import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.task.AnimationTask;
import gg.aquatic.aquaticseries.lib.util.argument.AquaticObjectArgument;
import gg.aquatic.aquaticseries.lib.util.argument.impl.PrimitiveObjectArgument;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholders;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class CommandTask extends AnimationTask {
    @Override
    public void run(Animation animation, @NotNull Map<String, ?> arguments, @NotNull BiFunction<Animation, String, String> biFunction) {
        var command = arguments.get("command");
        var commands = arguments.get("commands");
        if (command != null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), biFunction.apply(animation, command.toString()));
            return;
        }
        if (commands != null) {
            var cmds = (List<String>)commands;
            for (String cmd : cmds) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), biFunction.apply(animation, cmd));
            }
        }
    }

    @NotNull
    @Override
    public List<AquaticObjectArgument<?>> arguments() {
        return List.of(
                new PrimitiveObjectArgument("command",null,false),
                new PrimitiveObjectArgument("commands",new ArrayList<>(),false)
        );
    }
}
