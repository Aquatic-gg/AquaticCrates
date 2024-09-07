package cz.larkyy.aquaticcrates.crate.price.types;

import cz.larkyy.aquaticcrates.crate.Key;
import cz.larkyy.aquaticcrates.player.CratePlayer;
import gg.aquatic.aquaticseries.lib.price.AbstractPrice;
import gg.aquatic.aquaticseries.lib.util.argument.AquaticObjectArgument;
import gg.aquatic.aquaticseries.lib.util.argument.impl.PrimitiveObjectArgument;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeyPrice extends AbstractPrice<Player> {

    public static final List<AquaticObjectArgument<?>> ARGUMENTS = new ArrayList<>();

    static {
        ARGUMENTS.add(new PrimitiveObjectArgument("amount", 1, false));
        ARGUMENTS.add(new PrimitiveObjectArgument("crate", null, false));
    }

    @Override
    public void take(Player player, @NotNull Map<String, ?> arguments) {
        String crateId;
        if (arguments.containsKey("crate") && arguments.get("crate") != null) {
            crateId = arguments.get("crate").toString();
        } else {
            return;
        }
        int amount = (int) arguments.get("amount");
        Key key = Key.get(crateId);
        CratePlayer cp = CratePlayer.get(player);
        for (int i = 0; i < amount; i++) {
            cp.takeKey(key);
        }
    }

    @Override
    public void give(Player player, @NotNull Map<String, ?> map) {

    }

    @Override
    public void set(Player player, @NotNull Map<String, ?> map) {

    }

    @Override
    public boolean has(Player player, @NotNull Map<String, ?> arguments) {
        String crateId;
        if (arguments.containsKey("crate") && arguments.get("crate") != null) {
            crateId = arguments.get("crate").toString();
        } else {
            return false;
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
        return (cp.hasKey(key, amount));
    }

    @NotNull
    @Override
    public List<AquaticObjectArgument<?>> arguments() {
        return List.of();
    }
}
