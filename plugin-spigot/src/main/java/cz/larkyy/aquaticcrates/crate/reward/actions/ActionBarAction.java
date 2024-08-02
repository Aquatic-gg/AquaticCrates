package cz.larkyy.aquaticcrates.crate.reward.actions;

import cz.larkyy.aquaticcrates.crate.reward.RewardAction;
import cz.larkyy.aquaticcrates.placeholders.Placeholders;
import cz.larkyy.aquaticcrates.utils.colors.Colors;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ActionBarAction extends RewardAction {

    @Override
    public void run(Player player, Map<String, Object> arguments, Placeholders placeholders) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                Colors.format(PlaceholderAPI.setPlaceholders(player, placeholders.replace(arguments.get("message").toString())))
        ));
    }

    @Override
    public Map<String, Object> readArguments(String string) {
        return new HashMap<>() {
            {
                put("message", string);
            }
        };
    }
}
