package cz.larkyy.aquaticcratestesting.crate.reward;

import cz.larkyy.aquaticcratestesting.item.CustomItem;
import org.bukkit.entity.Player;

import java.util.List;

public class Reward {

    private final String identifier;
    private final CustomItem item;
    private final List<RewardAction> actions;
    private final double chance;
    private final boolean giveItem;
    private final String permission;
    private final List<String> hologram;
    private final double hologramYOffset;

    public Reward(String identifier, CustomItem item, double chance, List<RewardAction> actions, String permission, boolean giveItem, List<String> hologram, double hologramYOffset) {
        this.identifier = identifier;
        this.item = item;
        this.chance = chance;
        this.actions = actions;
        this.giveItem = giveItem;
        this.permission = permission;
        this.hologram = hologram;
        this.hologramYOffset = hologramYOffset;
    }

    public void give(Player player) {
        if (giveItem) {
            item.giveItem(player);
        }
        actions.forEach(a -> a.run(player));
    }

    public List<String> getHologram() {
        return hologram;
    }

    public double getHologramYOffset() {
        return hologramYOffset;
    }

    public String getIdentifier() {
        return identifier;
    }

    public CustomItem getItem() {
        return item;
    }

    public double getChance() {
        return chance;
    }

    public String getPermission() {
        return permission;
    }
}
