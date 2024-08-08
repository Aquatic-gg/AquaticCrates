package cz.larkyy.aquaticcrates.crate.reward.actions;

import cz.larkyy.aquaticcrates.crate.reward.RewardAction;
import cz.larkyy.aquaticcrates.placeholders.Placeholders;
import gg.aquatic.aquaticseries.lib.StringExtKt;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BroadcastAction extends RewardAction {
    @Override
    public void run(Player player, Map<String, Object> arguments, Placeholders placeholders) {
        var msg = PlaceholderAPI.setPlaceholders(player, placeholders.replace(arguments.get("message").toString()));
        StringExtKt.toAquatic(msg).broadcast();
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
