package cz.larkyy.aquaticcratestesting.crate.reward.condition;

import cz.larkyy.aquaticcratestesting.crate.Crate;
import org.bukkit.entity.Player;

import java.util.Map;

public class ConfiguredRewardCondition {
    private final RewardCondition condition;
    private final Map<String,Object> values;

    public ConfiguredRewardCondition(RewardCondition condition, Map<String,Object> values) {
        this.condition = condition;
        this.values = values;
    }

    public boolean check(Player player, Crate crate) {
        boolean negate = false;
        if (values.containsKey("negate")) {
            negate = (boolean) values.get("negate");
        }
        boolean value = condition.check(player,crate,values);
        if (negate) {
            return !value;
        } else {
            return value;
        }
    }
}
