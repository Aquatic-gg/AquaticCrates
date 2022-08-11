package cz.larkyy.aquaticcratestesting.crate.reward;

import cz.larkyy.aquaticcratestesting.crate.reward.actions.CommandAction;
import cz.larkyy.aquaticcratestesting.crate.reward.actions.MessageAction;
import org.bukkit.entity.Player;

public abstract class RewardAction {

    public abstract void run(Player player);

    public static RewardAction get(String str) {
        String args;
        if (str.startsWith("[message]")) {
            args = str.substring(9).trim();
            return new MessageAction(args);
        } else if (str.startsWith("[command]")) {
            args = str.substring(9).trim();
            return new CommandAction(args);
        }
        return null;
    }
}
