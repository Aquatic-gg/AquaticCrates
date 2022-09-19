package cz.larkyy.aquaticcratestesting.crate.reward.actions;

import cz.larkyy.aquaticcratestesting.crate.reward.RewardAction;
import org.bukkit.entity.Player;
import xyz.larkyy.colorutils.Colors;

public class MessageAction extends RewardAction {

    private final String message;

    public MessageAction(String message) {
        this.message = message;
    }

    @Override
    public void run(Player player) {
        player.sendMessage(Colors.format(message.replace("%player%",player.getName())));
    }
}
