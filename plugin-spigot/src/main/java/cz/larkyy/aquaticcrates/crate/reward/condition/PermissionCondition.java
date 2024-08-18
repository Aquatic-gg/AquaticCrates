package cz.larkyy.aquaticcrates.crate.reward.condition;

import gg.aquatic.aquaticseries.lib.requirement.player.PlayerRequirement;
import gg.aquatic.aquaticseries.lib.util.AquaticObjectArgument;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PermissionCondition extends PlayerRequirement {

    public static final List<AquaticObjectArgument> ARGUMENTS = new ArrayList<>();
    static {
        ARGUMENTS.add(new AquaticObjectArgument("permission","acrates.reward.collect",true));
        ARGUMENTS.add(new AquaticObjectArgument("negate",false,false));
    }

    @Override
    public boolean check(Player player, @NotNull Map<String, ?> arguments) {
        return player.hasPermission((String) arguments.get("permission"));
    }

    @NotNull
    @Override
    public List<AquaticObjectArgument> arguments() {
        return ARGUMENTS;
    }
}
