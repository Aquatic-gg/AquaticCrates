package cz.larkyy.aquaticcrates.animation.task.impl;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.task.AnimationTask;
import gg.aquatic.aquaticseries.lib.StringExtKt;
import gg.aquatic.aquaticseries.lib.util.argument.AquaticObjectArgument;
import gg.aquatic.aquaticseries.lib.util.argument.impl.PrimitiveObjectArgument;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholders;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class SendTitleTask extends AnimationTask {
    @Override
    public void run(Animation animation, @NotNull Map<String, ?> arguments, @NotNull BiFunction<Animation, String, String> biFunction) {
        AquaticCrates.aquaticSeriesLib.getAdapter().getTitleAdapter().send(
                animation.getPlayer(),
                StringExtKt.toAquatic(arguments.get("title").toString()),
                StringExtKt.toAquatic(arguments.get("subtitle").toString()),
                (int)arguments.get("in"),
                (int)arguments.get("stay"),
                (int)arguments.get("out")
        );
    }

    @NotNull
    @Override
    public List<AquaticObjectArgument<?>> arguments() {
        return List.of(
                new PrimitiveObjectArgument("in",0,false),
                new PrimitiveObjectArgument("out",0,false),
                new PrimitiveObjectArgument("stay",0,false),
                new PrimitiveObjectArgument("title","",false),
                new PrimitiveObjectArgument("subtitle","",false)
        );
    }
}
