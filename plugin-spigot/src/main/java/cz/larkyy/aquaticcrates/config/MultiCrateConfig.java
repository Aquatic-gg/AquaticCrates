package cz.larkyy.aquaticcrates.config;

import cz.larkyy.aquaticcrates.crate.MultiCrate;
import cz.larkyy.aquaticcrates.crate.inventories.settings.CustomInventorySettings;
import cz.larkyy.aquaticcrates.crate.inventories.settings.MultiPreviewGUISettings;
import cz.larkyy.aquaticcrates.crate.model.ModelAnimation;
import cz.larkyy.aquaticcrates.crate.model.ModelAnimations;
import cz.larkyy.aquaticcrates.crate.model.ModelSettings;
import gg.aquatic.aquaticseries.lib.ConfigExtKt;
import gg.aquatic.aquaticseries.lib.StringExtKt;
import gg.aquatic.aquaticseries.lib.action.player.PlayerActionSerializer;
import gg.aquatic.aquaticseries.lib.inventory.lib.component.Button;
import gg.aquatic.aquaticseries.lib.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
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
                getConfiguration().getDouble("hologram-y-offset",0),
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

    private List<String> loadHologram(String path) {
        if (!getConfiguration().contains(path)) {
            return new ArrayList<>();
        }
        return getConfiguration().getStringList(path);
    }

    private MultiPreviewGUISettings loadMultiPreviewGUI(String crateId) {
        if (!getConfiguration().contains("preview") || !getConfiguration().getBoolean("preview.enabled",true)) {
            return null;
        }

        String title;
        if (getConfiguration().contains("preview.title")) {
            title = getConfiguration().getString("preview.title");
        } else {
            title = crateId+" Preview";
        }

        var clearBottomInventory = getConfiguration().getBoolean("preview.clear-bottom-inventory", false);
        var items = loadInventoryButtons(getConfiguration().getConfigurationSection("preview.items"));

        return new MultiPreviewGUISettings(
                new CustomInventorySettings(title, getConfiguration().getInt("preview.size",54), items),
                clearBottomInventory
        );
    }

    private Map<String, Button> loadInventoryButtons(ConfigurationSection section) {
        Map<String, Button> buttons = new HashMap<>();
        if (section == null) return buttons;
        for (String key : section.getKeys(false)) {
            var buttonSection = section.getConfigurationSection(key);
            assert buttonSection != null;
            var button = loadButton(buttonSection);
            buttons.put(key, button);
        }
        return buttons;
    }

    private Button loadButton(String path) {
        if (!getConfiguration().contains(path)) {
            return null;
        }
        return loadButton(getConfiguration().getConfigurationSection(path));
    }

    private Button loadButton(ConfigurationSection section) {
        if (section == null) {
            return null;
        }
        var button = Button.Companion.fromConfig(section);
        var sections = ConfigExtKt.getSectionList(section, "click-actions");
        var actions = PlayerActionSerializer.INSTANCE.fromSections(sections);

        button.setOnClick(e -> {
            e.getOriginalEvent().setCancelled(true);
            var placeholders = new gg.aquatic.aquaticseries.lib.util.placeholder.Placeholders();
            placeholders.addPlaceholder(new gg.aquatic.aquaticseries.lib.util.placeholder.Placeholder("%player%",e.getOriginalEvent().getWhoClicked().getName()));
            actions.forEach(action -> {
                action.run((Player) e.getOriginalEvent().getWhoClicked(),placeholders);
            });
        });

        button.setPriority(1);

        return button;
    }
    private CustomItem loadItem(String path) {
        return CustomItem.Companion.loadFromYaml(getConfiguration().getConfigurationSection(path));
    }

}
