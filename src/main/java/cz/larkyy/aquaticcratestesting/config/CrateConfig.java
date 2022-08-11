package cz.larkyy.aquaticcratestesting.config;

import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import cz.larkyy.aquaticcratestesting.crate.reward.RewardAction;
import cz.larkyy.aquaticcratestesting.item.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CrateConfig extends Config {

    private final String identifier;

    public CrateConfig(JavaPlugin main, File f) {
        super(main, f);
        this.identifier = f.getName().substring(0,f.getName().length()-4);
    }

    public Crate loadCrate() {
        return new Crate(
                identifier,
                loadKey(),
                getConfiguration().getString("model"),
                loadRewards()
        );
    }

    private CustomItem loadKey() {
        return loadItem("key");
    }

    private CustomItem loadItem(String path) {
        return CustomItem.create(
                getConfiguration().getString(path+".material"),
                getConfiguration().getString(path+".display-name"),
                getConfiguration().getStringList(path+".lore")
                );
    }

    private List<Reward> loadRewards() {
        List<Reward> list = new ArrayList<>();
        if (!getConfiguration().contains("rewards")) {
            return list;
        }
        getConfiguration().getConfigurationSection("rewards").getKeys(false).forEach(rStr -> {
            list.add(loadReward("rewards."+rStr));
        });
        return list;
    }

    private Reward loadReward(String path) {
        CustomItem item = loadItem(path+".item");
        double chance = getConfiguration().getDouble(path+".chance");
        String permission = getConfiguration().getString(path+".permission");
        boolean giveItem = getConfiguration().getBoolean(path+".give-item",false);
        Bukkit.broadcastMessage("give-item is "+giveItem);
        return new Reward(item,chance,loadRewardActions(path+".actions"),permission,giveItem);
    }

    private List<RewardAction> loadRewardActions(String path) {
        List<RewardAction> list = new ArrayList<>();
        for (String aStr : getConfiguration().getStringList(path)) {
            RewardAction a = RewardAction.get(aStr);
            if (a == null) continue;
            list.add(a);
        }
        return list;
    }
}
