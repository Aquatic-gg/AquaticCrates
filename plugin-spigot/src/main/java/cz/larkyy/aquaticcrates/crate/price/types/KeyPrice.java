package cz.larkyy.aquaticcrates.crate.price.types;

import cz.larkyy.aquaticcrates.animation.task.TaskArgument;
import cz.larkyy.aquaticcrates.crate.Crate;
import cz.larkyy.aquaticcrates.crate.Key;
import cz.larkyy.aquaticcrates.crate.price.OpenPrice;
import cz.larkyy.aquaticcrates.player.CratePlayer;
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

        if (key.isMustBeHeld()) {
            var is = player.getInventory().getItemInMainHand();
            if (!key.isItemKey(is)) return false;
            return (is.getAmount() >= amount);
        }

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
