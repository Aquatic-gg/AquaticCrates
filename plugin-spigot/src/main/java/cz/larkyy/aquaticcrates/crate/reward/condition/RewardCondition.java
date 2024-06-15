package cz.larkyy.aquaticcrates.crate.reward.condition;

import cz.larkyy.aquaticcrates.animation.task.TaskArgument;
import cz.larkyy.aquaticcrates.crate.Crate;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public abstract class RewardCondition {
    public abstract boolean check(Player player, Crate crate, Map<String,Object> arguments);
    public abstract List<TaskArgument> getArgs();
}
