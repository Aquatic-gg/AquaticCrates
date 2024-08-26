package cz.larkyy.aquaticcrates.animation.task.impl2;

import cz.larkyy.aquaticcrates.animation.Animation;
import gg.aquatic.aquaticseries.lib.action.AbstractAction;
import gg.aquatic.aquaticseries.lib.util.argument.AquaticObjectArgument;
import gg.aquatic.aquaticseries.lib.util.argument.impl.PrimitiveObjectArgument;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholders;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandTask extends AbstractAction<Animation> {
    @Override
    public void run(Animation animation, @NotNull Map<String, ?> arguments, @NotNull Placeholders placeholders) {
        var command = arguments.get("command");
        var commands = arguments.get("commands");
        if (command != null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), placeholders.replace(command.toString()));
            return;
        }
        if (commands != null) {
            var cmds = (List<String>)commands;
            for (String cmd : cmds) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), placeholders.replace(cmd));
            }
        }
    }

    @NotNull
    @Override
    public List<AquaticObjectArgument<?>> arguments() {
        return List.of(
                new PrimitiveObjectArgument("delay",0,false),
                new PrimitiveObjectArgument("command",null,false),
                new PrimitiveObjectArgument("commands",new ArrayList<>(),false)
        );
    }
}
