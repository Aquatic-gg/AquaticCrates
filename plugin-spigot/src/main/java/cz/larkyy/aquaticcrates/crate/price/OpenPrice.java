package cz.larkyy.aquaticcrates.crate.price;

import cz.larkyy.aquaticcrates.animation.task.TaskArgument;
import cz.larkyy.aquaticcrates.crate.Crate;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public abstract class OpenPrice {

    public abstract boolean check(Player player, Crate crate, Map<String,Object> arguments);
    public abstract void take(Player player, Crate crate, Map<String,Object> arguments);

    public abstract List<TaskArgument> getArgs();

}
