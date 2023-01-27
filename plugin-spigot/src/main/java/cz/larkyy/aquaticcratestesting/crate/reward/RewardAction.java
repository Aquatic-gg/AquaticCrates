package cz.larkyy.aquaticcratestesting.crate.reward;
import cz.larkyy.aquaticcratestesting.placeholders.Placeholders;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class RewardAction {

    public abstract void run(Player player, Map<String,Object> arguments, Placeholders placeholders);
    public abstract Map<String,Object> readArguments(String string);
}
