package cz.larkyy.aquaticcratestesting.crate.reward.actions;

import cz.larkyy.aquaticcratestesting.crate.reward.RewardAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandAction extends RewardAction {

    private final String args;

    public CommandAction(String args) {
        this.args = args;
    }

    @Override
    public void run(Player player) {
        Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                args.replace("%player%", player.getName())
        );
    }
}
