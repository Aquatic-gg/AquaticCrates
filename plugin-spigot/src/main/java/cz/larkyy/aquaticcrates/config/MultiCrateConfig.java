package cz.larkyy.aquaticcrates.config;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.crate.MultiCrate;
import cz.larkyy.aquaticcrates.crate.inventories.MultiPreviewGUI;
import cz.larkyy.aquaticcrates.crate.inventories.settings.MultiPreviewGUISettings;
import cz.larkyy.aquaticcrates.crate.model.ModelAnimation;
import cz.larkyy.aquaticcrates.crate.model.ModelAnimations;
import cz.larkyy.aquaticcrates.crate.model.ModelSettings;
import cz.larkyy.aquaticcrates.menu.Menu;
import cz.larkyy.aquaticcrates.menu.MenuItem;
import gg.aquatic.aquaticseries.lib.StringExtKt;
import gg.aquatic.aquaticseries.lib.adapt.AquaticString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.itemlibrary.CustomItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                loadMultiPreviewGUI(identifier)

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

        AquaticString title;
        if (getConfiguration().contains("preview.title")) {
            title = StringExtKt.toAquatic(getConfiguration().getString("preview.title"));
        } else {
            title = StringExtKt.toAquatic(crateId+" Preview");
        }
        Menu.Builder builder =Menu.builder(AquaticCrates.instance())
                .size(getConfiguration().getInt("preview.size",54))
                .title(title.getString());

        if (getConfiguration().contains("preview.items")) {
            for (String str : getConfiguration().getConfigurationSection("preview.items").getKeys(false)) {
                var item = loadMenuItem(str,"preview.items."+str);
                if (item != null) {
                    builder.addItem(item);
                }
            }
        }

        return null;
    }

    private MenuItem loadMenuItem(String identifier, String path) {

        CustomItem item = loadItem(path);
        if (item == null) {
            Bukkit.getConsoleSender().sendMessage("§cMenu Item "+path+" could not be loaded, because the item is null!");
            return null;
        }
        List<Integer> slots;
        if (getConfiguration().contains(path+".slot")) {
            slots = Arrays.asList(getConfiguration().getInt(path+".slot"));
        } else {
            slots = getConfiguration().getIntegerList(path+".slots");
        }

        return MenuItem.builder(identifier,item.getItem())
                .slots(slots)
                .build();
    }
    private CustomItem loadItem(String path) {
        return CustomItem.loadFromYaml(getConfiguration(),path);
    }

}
