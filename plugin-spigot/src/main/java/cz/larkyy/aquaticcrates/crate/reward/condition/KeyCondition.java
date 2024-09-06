package cz.larkyy.aquaticcrates.crate.reward.condition;

import cz.larkyy.aquaticcrates.crate.Crate;
import cz.larkyy.aquaticcrates.player.CratePlayer;
import gg.aquatic.aquaticseries.lib.requirement.AbstractRequirement;
import gg.aquatic.aquaticseries.lib.util.argument.AquaticObjectArgument;
import gg.aquatic.aquaticseries.lib.util.argument.impl.PrimitiveObjectArgument;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeyCondition extends AbstractRequirement<Player> {

    public static final List<AquaticObjectArgument<?>> ARGUMENTS = new ArrayList<>();
    static {
        ARGUMENTS.add(new PrimitiveObjectArgument("crate-id",null,true));
        ARGUMENTS.add(new PrimitiveObjectArgument("negate",false,false));
    }

    @Override
    public boolean check(Player player, @NotNull Map<String, ?> map) {
        var crateId = map.get("crate-id");
        if (crateId == null) return false;

        var crate = Crate.get(crateId.toString());
        if (crate == null) return false;
        var cratePlayer = CratePlayer.get(player);
        if (cratePlayer == null) return false;
        return cratePlayer.hasKey(crate.getKey(),1);
    }

    @NotNull
    @Override
    public List<AquaticObjectArgument<?>> arguments() {
        return ARGUMENTS;
    }
}
