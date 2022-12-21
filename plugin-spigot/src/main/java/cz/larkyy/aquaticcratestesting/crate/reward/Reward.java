package cz.larkyy.aquaticcratestesting.crate.reward;

import org.bukkit.entity.Player;
import xyz.larkyy.itemlibrary.CustomItem;

import java.util.List;

public class Reward {

    private final String identifier;
    private final CustomItem item;
    private final CustomItem previewItem;
    private final List<RewardAction> actions;
    private final double chance;
    private final boolean giveItem;
    private final String permission;
    private final List<String> hologram;
    private final double hologramYOffset;

    public Reward(String identifier, CustomItem item, CustomItem previewItem, double chance, List<RewardAction> actions, String permission, boolean giveItem, List<String> hologram, double hologramYOffset) {
        this.identifier = identifier;
        this.item = item;
        this.chance = chance;
        this.actions = actions;
        this.giveItem = giveItem;
        this.permission = permission;
        this.hologram = hologram;
        this.hologramYOffset = hologramYOffset;
        this.previewItem = previewItem;
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

    public CustomItem getPreviewItem() {
        return previewItem == null ? item : previewItem;
    }

    public double getChance() {
        return chance;
    }

    public String getPermission() {
        return permission;
    }
}
