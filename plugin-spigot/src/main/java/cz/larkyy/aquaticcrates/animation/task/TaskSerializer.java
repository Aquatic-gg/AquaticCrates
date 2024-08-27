package cz.larkyy.aquaticcrates.animation.task;

import cz.larkyy.aquaticcrates.animation.Animation;
import gg.aquatic.aquaticseries.lib.ConfigExtKt;
import gg.aquatic.aquaticseries.lib.action.ActionTypes;
import gg.aquatic.aquaticseries.lib.action.ConfiguredAction;
import gg.aquatic.aquaticseries.lib.util.argument.AquaticObjectArgument;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class TaskSerializer {

    public static HashMap<String,AnimationTask> tasks = new HashMap<>();

    public static TreeMap<Integer, List<ConfiguredAction<Animation>>> load(ConfigurationSection section) {
        var actions = new TreeMap<Integer, List<ConfiguredAction<Animation>>>();
        for (String key : section.getKeys(false)) {
            var time = Integer.parseInt(key);
            for (ConfigurationSection configurationSection : ConfigExtKt.getSectionList(section,key)) {
                var type = configurationSection.getString("type");
                if (type == null) continue;
                var action = tasks.get(type.toLowerCase());
                if (action == null) continue;
                var arguments = action.arguments();
                var args = AquaticObjectArgument.Companion.loadRequirementArguments(configurationSection,arguments);
                var configuredAction = new ConfiguredAction<>(action,args);
                List<ConfiguredAction<Animation>> list;
                if (!actions.containsKey(time)) {
                    list = new ArrayList<>();
                    actions.put(time,list);
                } else {
                    list = actions.get(time);
                }
                list.add(configuredAction);
            }
        }
        return actions;
    }

}
