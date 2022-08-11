package cz.larkyy.aquaticcratestesting.crate.reward;

import cz.larkyy.aquaticcratestesting.item.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class Reward {

    private final CustomItem item;
    private final List<RewardAction> actions;
    private final double chance;
    private final boolean giveItem;
    private final String permission;

    public Reward(CustomItem item, double chance, List<RewardAction> actions, String permission, boolean giveItem) {
        this.item = item;
        this.chance = chance;
        this.actions = actions;
        this.giveItem = giveItem;
        this.permission = permission;
    }

    public void give(Player player) {
        if (giveItem) {
            item.giveItem(player);
        }
        actions.forEach(a -> a.run(player));
    }

    public double getChance() {
        return chance;
    }

    public String getPermission() {
        return permission;
    }
}
