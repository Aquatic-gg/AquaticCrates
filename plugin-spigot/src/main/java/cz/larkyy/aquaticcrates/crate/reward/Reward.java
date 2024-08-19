package cz.larkyy.aquaticcrates.crate.reward;

import gg.aquatic.aquaticseries.lib.action.ConfiguredAction;
import gg.aquatic.aquaticseries.lib.chance.IChance;
import gg.aquatic.aquaticseries.lib.requirement.ConfiguredRequirement;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholder;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholders;
import org.bukkit.entity.Player;
import xyz.larkyy.itemlibrary.CustomItem;

import java.util.List;

public class Reward implements IChance {

    private final String identifier;
    private final CustomItem item;
    private final CustomItem previewItem;
    private final String model;
    private final float modelYaw;
    private final List<ConfiguredAction<Player>> actions;
    private final double chance;
    private final boolean giveItem;
    private final String modelAnimation;
    private final String permission;
    private final List<ConfiguredRequirement<Player>> winConditions;
    private final List<String> hologram;
    private final double hologramYOffset;

    public Reward(String identifier, CustomItem item, CustomItem previewItem, double chance, List<ConfiguredAction<Player>> actions,
                  String permission, boolean giveItem, List<String> hologram, double hologramYOffset, String modelAnimation,
                  List<ConfiguredRequirement<Player>> winConditions, String model, float modelYaw) {
        this.identifier = identifier;
        this.item = item;
        this.chance = chance;
        this.actions = actions;
        this.giveItem = giveItem;
        this.permission = permission;
        this.modelAnimation = modelAnimation;
        this.hologram = hologram;
        this.hologramYOffset = hologramYOffset;
        this.previewItem = previewItem;
        this.winConditions = winConditions;
        this.model = model;
        this.modelYaw = modelYaw;
    }

    public float getModelYaw() {
        return modelYaw;
    }

    public void give(Player player) {
        if (giveItem) {
            var is = item.getItem();
            var map = player.getInventory().addItem(is);
            map.forEach((i,is2) -> {
                player.getLocation().getWorld().dropItem(player.getLocation(),is2);
            });
        }
        Placeholders placeholders = new Placeholders();

        placeholders.addPlaceholder(new Placeholder("%player%",player.getName()));
        placeholders.addPlaceholder(new Placeholder("%reward%",getPreviewItem().getItem().getItemMeta().getDisplayName()));
        placeholders.addPlaceholder(new Placeholder("%chance%",chance+""));
        actions.forEach(a -> a.run(player,placeholders));
    }

    public String getModel() {
        return model;
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

    public String getModelAnimation() {
        return modelAnimation;
    }

    public double chance() {
        return chance;
    }

    public String getPermission() {
        return permission;
    }

    public List<ConfiguredRequirement<Player>> getWinConditions() {
        return winConditions;
    }
}
