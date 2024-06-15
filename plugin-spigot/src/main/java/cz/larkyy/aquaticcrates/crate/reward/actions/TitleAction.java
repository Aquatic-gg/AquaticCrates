package cz.larkyy.aquaticcrates.crate.reward.actions;

import cz.larkyy.aquaticcrates.crate.reward.RewardAction;
import cz.larkyy.aquaticcrates.placeholders.Placeholders;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TitleAction extends RewardAction {


    @Override
    public void run(Player player, Map<String,Object> arguments, Placeholders placeholders) {
        player.sendTitle(
                PlaceholderAPI.setPlaceholders(player, placeholders.replace(arguments.get("title").toString())),
                PlaceholderAPI.setPlaceholders(player, placeholders.replace(arguments.get("subtitle").toString())),
                (int)arguments.get("in"),
                (int)arguments.get("stay"),
                (int)arguments.get("out"));
    }

    @Override
    public Map<String, Object> readArguments(String string) {
        String[] arguments = string.split("-\\|\\|-");
        String[] lines = arguments[0].split("\\n");

        int in = 10;
        int stay = 40;
        int out = 10;

        if (arguments.length > 1) {
            String[] titleValues = arguments[1].split(";");
            in = Integer.parseInt(titleValues[0]);
            stay = Integer.parseInt(titleValues[1]);
            out = Integer.parseInt(titleValues[2]);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("title", lines[0]);
        map.put("subtitle",(lines.length == 1 ? "" : lines[1]));
        map.put("in",in);
        map.put("stay",stay);
        map.put("out",out);

        return map;
    }
}
