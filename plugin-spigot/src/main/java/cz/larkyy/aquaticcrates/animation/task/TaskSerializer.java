package cz.larkyy.aquaticcrates.animation.task;

import cz.larkyy.aquaticcrates.animation.Animation;
import gg.aquatic.aquaticseries.lib.ConfigExtKt;
import gg.aquatic.aquaticseries.lib.action.ActionTypes;
import gg.aquatic.aquaticseries.lib.action.ConfiguredAction;
import gg.aquatic.aquaticseries.lib.util.argument.AquaticObjectArgument;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class TaskSerializer {

    public static TreeMap<Integer, List<ConfiguredAction<Animation>>> load(ConfigurationSection section) {
        var actions = new TreeMap<Integer, List<ConfiguredAction<Animation>>>();
        for (String key : section.getKeys(false)) {
            var time = Integer.parseInt(key);
            for (ConfigurationSection configurationSection : ConfigExtKt.getSectionList(section,key)) {
                var type = configurationSection.getString(key+".type");
                if (type == null) continue;
                var action = ActionTypes.INSTANCE.getActions().get(type);
                if (action == null) continue;
                if (!(action instanceof AnimationTask animationTask)) continue;
                var arguments = animationTask.arguments();
                var args = AquaticObjectArgument.Companion.loadRequirementArguments(configurationSection,arguments);
                var configuredAction = new ConfiguredAction<>(animationTask,args);
                actions.getOrDefault(time,new ArrayList<>()).add(configuredAction);
            }
        }
        return actions;
    }

}
