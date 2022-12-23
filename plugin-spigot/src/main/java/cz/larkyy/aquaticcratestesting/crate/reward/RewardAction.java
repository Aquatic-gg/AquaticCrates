package cz.larkyy.aquaticcratestesting.crate.reward;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class RewardAction {

    public abstract void run(Player player, Map<String,Object> arguments);
    public abstract Map<String,Object> readArguments(String string);
}
