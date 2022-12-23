package cz.larkyy.aquaticcratestesting.crate.reward;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.crate.reward.actions.ActionBarAction;
import cz.larkyy.aquaticcratestesting.crate.reward.actions.CommandAction;
import cz.larkyy.aquaticcratestesting.crate.reward.actions.MessageAction;
import cz.larkyy.aquaticcratestesting.crate.reward.actions.TitleAction;

import java.util.HashMap;
import java.util.Map;

public class RewardActions {

    private final Map<String, RewardAction> actionTypes = new HashMap<>() {
        {
            put("title",new TitleAction());
            put("message", new MessageAction());
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
        return AquaticCratesTesting.getRewardActions();
    }

    public Map<String, RewardAction> getActionTypes() {
        return actionTypes;
    }
}
