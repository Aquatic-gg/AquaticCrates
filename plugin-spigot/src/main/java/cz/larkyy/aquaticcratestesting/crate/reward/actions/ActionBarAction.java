package cz.larkyy.aquaticcratestesting.crate.reward.actions;

import cz.larkyy.aquaticcratestesting.crate.reward.RewardAction;
import cz.larkyy.aquaticcratestesting.placeholders.Placeholders;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import xyz.larkyy.colorutils.Colors;

import java.util.HashMap;
import java.util.Map;

public class ActionBarAction extends RewardAction {

    @Override
    public void run(Player player, Map<String, Object> arguments, Placeholders placeholders) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                PlaceholderAPI.setPlaceholders(player, placeholders.replace(arguments.get("message").toString()))
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
