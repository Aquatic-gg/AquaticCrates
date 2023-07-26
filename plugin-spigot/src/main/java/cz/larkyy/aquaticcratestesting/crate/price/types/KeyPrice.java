package cz.larkyy.aquaticcratestesting.crate.price.types;

import cz.larkyy.aquaticcratestesting.animation.task.TaskArgument;
import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.crate.Key;
import cz.larkyy.aquaticcratestesting.crate.price.OpenPrice;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeyPrice extends OpenPrice {

    public static final List<TaskArgument> ARGUMENTS = new ArrayList<>();
    static {
        ARGUMENTS.add(new TaskArgument("amount",1,false));
        ARGUMENTS.add(new TaskArgument("crate",null,false));
    }

    @Override
    public boolean check(Player player, Crate crate, Map<String, Object> arguments) {
        String crateId;
        if (arguments.containsKey("crate") && arguments.get("crate") != null) {
            crateId = arguments.get("crate").toString();
        } else {
            crateId = crate.getIdentifier();
        }
        Key key = Key.get(crateId);

        if (key == null) {
            return false;
        }
        int amount = (int) arguments.get("amount");

        CratePlayer cp = CratePlayer.get(player);
        return (cp.hasKey(key,amount));
    }

    @Override
    public void take(Player player, Crate crate, Map<String, Object> arguments) {
        String crateId;
        if (arguments.containsKey("crate") && arguments.get("crate") != null) {
            crateId = arguments.get("crate").toString();
        } else {
            crateId = crate.getIdentifier();
        }
        int amount = (int) arguments.get("amount");
        Key key = Key.get(crateId);
        CratePlayer cp = CratePlayer.get(player);
        for (int i = 0; i < amount; i++) {
            cp.takeKey(key);
        }
    }

    @Override
    public List<TaskArgument> getArgs() {
        return ARGUMENTS;
    }
}
