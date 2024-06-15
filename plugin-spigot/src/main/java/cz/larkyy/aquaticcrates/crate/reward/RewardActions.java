package cz.larkyy.aquaticcrates.crate.reward;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.crate.reward.actions.*;

import java.util.HashMap;
import java.util.Map;

public class RewardActions {

    private final Map<String, RewardAction> actionTypes = new HashMap<>() {
        {
            put("title",new TitleAction());
            put("message", new MessageAction());
            put("broadcast", new BroadcastAction());
            put("command", new CommandAction());
            put("actionbar", new ActionBarAction());
        }
    };

    public void registerAction(String id, RewardAction action) {
        actionTypes.put(id,action);
    }

    public void unregisterAction(String id) {
        actionTypes.remove(id);
    }

    public RewardAction getAction(String string) {
        return actionTypes.get(string.toLowerCase());
    }

    public static RewardActions inst() {
        return AquaticCrates.getRewardActions();
    }

    public Map<String, RewardAction> getActionTypes() {
        return actionTypes;
    }
}
