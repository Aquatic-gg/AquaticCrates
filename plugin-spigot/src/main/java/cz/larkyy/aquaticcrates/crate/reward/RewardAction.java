package cz.larkyy.aquaticcrates.crate.reward;
import cz.larkyy.aquaticcrates.placeholders.Placeholders;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class RewardAction {

    public abstract void run(Player player, Map<String,Object> arguments, Placeholders placeholders);
    public abstract Map<String,Object> readArguments(String string);
}
