package cz.larkyy.aquaticcratestesting.crate.reward.actions;

import cz.larkyy.aquaticcratestesting.crate.reward.RewardAction;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import xyz.larkyy.colorutils.Colors;

import java.util.HashMap;
import java.util.Map;

public class ActionBarAction extends RewardAction {

    @Override
    public void run(Player player, Map<String, Object> arguments) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                PlaceholderAPI.setPlaceholders(player, arguments.get("message").toString().replace("%player%", player.getName()))
        ));
    }

    @Override
    public Map<String, Object> readArguments(String string) {
        return new HashMap<>() {
            {
                put("message", Colors.format(string));
            }
        };
    }
}
