package cz.larkyy.aquaticcrates.crate.reward;

import cz.larkyy.aquaticcrates.hologram.settings.AquaticHologramSettings;
import gg.aquatic.aquaticseries.lib.action.ConfiguredAction;
import gg.aquatic.aquaticseries.lib.betterhologram.AquaticHologram;
import gg.aquatic.aquaticseries.lib.chance.IChance;
import gg.aquatic.aquaticseries.lib.item.CustomItem;
import gg.aquatic.aquaticseries.lib.requirement.ConfiguredRequirement;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholder;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholders;
import org.bukkit.entity.Player;

import java.util.List;

public class Reward implements IChance {

    private final String identifier;
    private final CustomItem item;
    private final String model;
    private final float modelYaw;
    private final List<ConfiguredAction<Player>> actions;
    private final double chance;
    private final boolean giveItem;
    private final String modelAnimation;
    private final List<ConfiguredRequirement<Player>> winConditions;
    private final AquaticHologramSettings hologram;
    private final String displayName;

    public Reward(String identifier, String displayName, CustomItem item, double chance, List<ConfiguredAction<Player>> actions, boolean giveItem, AquaticHologramSettings hologram, String modelAnimation,
                  List<ConfiguredRequirement<Player>> winConditions, String model, float modelYaw) {
        this.identifier = identifier;
        this.item = item;
        if (displayName == null) {
            this.displayName = item.getItem().getItemMeta().getDisplayName();
        } else {
            this.displayName = displayName;
        }
        this.chance = chance;
        this.actions = actions;
        this.giveItem = giveItem;
        this.modelAnimation = modelAnimation;
        this.hologram = hologram;
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
            map.forEach((i, is2) -> {
                player.getLocation().getWorld().dropItem(player.getLocation(), is2);
            });
        }
        actions.forEach(a -> a.run(player, ((player1, s) -> s.replace("%player%", player.getName())
                .replace("%reward%", displayName)
                .replace("%chance%", chance + ""))));
    }

    public String getModel() {
        return model;
    }

    public AquaticHologramSettings getHologram() {
        return hologram;
    }

    public String getIdentifier() {
        return identifier;
    }

    public CustomItem getItem() {
        return item;
    }

    public String getModelAnimation() {
        return modelAnimation;
    }

    public double chance() {
        return chance;
    }

    public List<ConfiguredRequirement<Player>> getWinConditions() {
        return winConditions;
    }

    public String getDisplayName() {
        return displayName;
    }
}
