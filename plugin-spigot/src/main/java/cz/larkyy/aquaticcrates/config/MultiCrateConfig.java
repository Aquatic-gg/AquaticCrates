package cz.larkyy.aquaticcrates.config;

import cz.larkyy.aquaticcrates.crate.MultiCrate;
import cz.larkyy.aquaticcrates.crate.inventories.settings.CustomInventorySettings;
import cz.larkyy.aquaticcrates.crate.inventories.settings.MultiPreviewGUISettings;
import cz.larkyy.aquaticcrates.crate.model.ModelAnimation;
import cz.larkyy.aquaticcrates.crate.model.ModelAnimations;
import cz.larkyy.aquaticcrates.crate.model.ModelSettings;
import gg.aquatic.aquaticseries.lib.StringExtKt;
import gg.aquatic.aquaticseries.lib.betterinventory2.serialize.InventorySerializer;
import gg.aquatic.aquaticseries.lib.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class MultiCrateConfig extends Config {

    private final String identifier;
    public MultiCrateConfig(JavaPlugin main, File f) {
        super(main, f);
        this.identifier = f.getName().substring(0,f.getName().length()-4);
    }

    public MultiCrate loadCrate() {
        MultiCrate c = new MultiCrate(
                identifier,
                StringExtKt.toAquatic(getConfiguration().getString("display-name",identifier)),
                loadModelSettings(),
                loadHologram("hologram"),
                getConfiguration().getStringList("crates"),
                getConfiguration().getInt("hitbox-height",1),
                getConfiguration().getInt("hitbox-width",1),
                loadMultiPreviewGUI(identifier),
                null

        );

        c.setBlockType(Material.valueOf(getConfiguration().getString("block-type","BARRIER").toUpperCase()));

        return c;
    }

    private ModelSettings loadModelSettings() {
        String modelId = getConfiguration().getString("model");
        int period = getConfiguration().getInt("idle-animation-period",-1);
        if (!getConfiguration().contains("idle-animations")) {
            return new ModelSettings(modelId, new ModelAnimations(new ArrayList<>(),period));
        }
        List<ModelAnimation> animations = new ArrayList<>();
        for (String key : getConfiguration().getConfigurationSection("idle-animations").getKeys(false)) {
            String animationId = getConfiguration().getString("idle-animations."+key+".animation");
            int animationLength = getConfiguration().getInt("idle-animations."+key+".length");
            animations.add(new ModelAnimation(animationId,animationLength));
        }
        return new ModelSettings(modelId,new ModelAnimations(animations,period));
    }

    private MultiPreviewGUISettings loadMultiPreviewGUI(String crateId) {
        if (!getConfiguration().contains("preview") || !getConfiguration().getBoolean("preview.enabled",true)) {
            return null;
        }

        var inventorySettings = InventorySerializer.INSTANCE.loadInventory(getConfiguration().getConfigurationSection("preview"));
        var clearBottomInventory = getConfiguration().getBoolean("preview.clear-bottom-inventory", false);
        //var items = loadInventoryButtons(getConfiguration().getConfigurationSection("preview.items"));

        return new MultiPreviewGUISettings(
                inventorySettings,
                clearBottomInventory
        );
    }

    private CustomItem loadItem(String path) {
        return CustomItem.Companion.loadFromYaml(getConfiguration().getConfigurationSection(path));
    }

}
