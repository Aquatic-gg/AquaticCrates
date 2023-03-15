package cz.larkyy.aquaticcratestesting.crate.reward.actions;

import cz.larkyy.aquaticcratestesting.crate.reward.RewardAction;
import cz.larkyy.aquaticcratestesting.placeholders.Placeholders;
import cz.larkyy.aquaticcratestesting.utils.colors.Colors;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CommandAction extends RewardAction {

    @Override
    public void run(Player player, Map<String, Object> arguments, Placeholders placeholders) {
        Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                PlaceholderAPI.setPlaceholders(player,placeholders.replace(arguments.get("message").toString()))
        );
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
