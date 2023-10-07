package cz.larkyy.aquaticcratestesting.crate.reward.condition.types;

import cz.larkyy.aquaticcratestesting.animation.task.TaskArgument;
import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.crate.reward.condition.RewardCondition;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PermissionCondition extends RewardCondition {

    public static final List<TaskArgument> ARGUMENTS = new ArrayList<>();
    static {
        ARGUMENTS.add(new TaskArgument("permission","acrates.reward.collect",true));
        ARGUMENTS.add(new TaskArgument("negate",false,false));
    }

    @Override
    public boolean check(Player player, Crate crate, Map<String, Object> arguments) {
        return player.hasPermission((String) arguments.get("permission"));
    }

    @Override
    public List<TaskArgument> getArgs() {
        return ARGUMENTS;
    }
}
