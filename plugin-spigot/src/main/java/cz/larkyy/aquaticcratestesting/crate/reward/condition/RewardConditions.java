package cz.larkyy.aquaticcratestesting.crate.reward.condition;

import cz.larkyy.aquaticcratestesting.AquaticCrates;
import cz.larkyy.aquaticcratestesting.crate.reward.condition.types.PermissionCondition;

import java.util.HashMap;
import java.util.Map;

public class RewardConditions {

    private final Map<String, RewardCondition> conditionMap = new HashMap<>() {
        {
            put("permission",new PermissionCondition());
        }
    };

    public void registerPriceType(String id, RewardCondition task) {
        conditionMap.put(id,task);
    }

    public void unregisterPriceType(String id) {
        conditionMap.remove(id);
    }

    public RewardCondition getPriceType(String string) {
        return conditionMap.get(string.toLowerCase());
    }

    public static RewardConditions inst() {
        return AquaticCrates.getRewardConditions();
    }

}
