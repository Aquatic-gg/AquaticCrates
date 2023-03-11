package cz.larkyy.aquaticcratestesting.crate.reward;

import cz.larkyy.aquaticcratestesting.placeholders.Placeholder;
import cz.larkyy.aquaticcratestesting.placeholders.Placeholders;
import org.bukkit.entity.Player;
import xyz.larkyy.itemlibrary.CustomItem;

import java.util.Arrays;
import java.util.List;

public class Reward {

    private final String identifier;
    private final CustomItem item;
    private final CustomItem previewItem;
    private final List<ConfiguredRewardAction> actions;
    private final double chance;
    private final boolean giveItem;
    private final String permission;
    private final List<String> hologram;
    private final double hologramYOffset;

    public Reward(String identifier, CustomItem item, CustomItem previewItem, double chance, List<ConfiguredRewardAction> actions, String permission, boolean giveItem, List<String> hologram, double hologramYOffset) {
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
            var is = item.getItem();
            var map = player.getInventory().addItem(is);
            map.forEach((i,is2) -> {
                player.getLocation().getWorld().dropItem(player.getLocation(),is2);
            });
        }
        Placeholders placeholders = new Placeholders(
                new Placeholder("%player%",player.getName()),
                new Placeholder("%reward%",getPreviewItem().getItem().getItemMeta().getDisplayName()),
                new Placeholder("%chance%",chance+"")
        );
        actions.forEach(a -> a.run(player,placeholders));
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
