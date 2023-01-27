package cz.larkyy.aquaticcratestesting.crate.reward;

import cz.larkyy.aquaticcratestesting.placeholders.Placeholders;
import org.bukkit.entity.Player;

import java.util.Map;

public class ConfiguredRewardAction {

    private final RewardAction action;
    private final Map<String,Object> args;

    public ConfiguredRewardAction(RewardAction action, String args) {
        this.action = action;
        this.args = action.readArguments(args);
    }

    public void run(Player player, Placeholders placeholders) {
        action.run(player,args, placeholders);
    }

}
