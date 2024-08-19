package cz.larkyy.aquaticcrates.crate.reward.condition;

import gg.aquatic.aquaticseries.lib.requirement.AbstractRequirement;
import gg.aquatic.aquaticseries.lib.util.argument.AquaticObjectArgument;
import gg.aquatic.aquaticseries.lib.util.argument.impl.PrimitiveObjectArgument;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PermissionCondition extends AbstractRequirement<Player> {

    public static final List<AquaticObjectArgument<?>> ARGUMENTS = new ArrayList<>();
    static {
        ARGUMENTS.add(new PrimitiveObjectArgument("permission","acrates.reward.collect",true));
        ARGUMENTS.add(new PrimitiveObjectArgument("negate",false,false));
    }

    @Override
    public boolean check(Player player, @NotNull Map<String, ?> arguments) {
        return player.hasPermission((String) arguments.get("permission"));
    }

    @NotNull
    @Override
    public List<AquaticObjectArgument<?>> arguments() {
        return ARGUMENTS;
    }
}
