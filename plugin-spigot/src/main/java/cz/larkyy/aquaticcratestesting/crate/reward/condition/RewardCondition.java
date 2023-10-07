package cz.larkyy.aquaticcratestesting.crate.reward.condition;

import cz.larkyy.aquaticcratestesting.animation.task.TaskArgument;
import cz.larkyy.aquaticcratestesting.crate.Crate;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public abstract class RewardCondition {
    public abstract boolean check(Player player, Crate crate, Map<String,Object> arguments);
    public abstract List<TaskArgument> getArgs();
}
