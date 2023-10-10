package cz.larkyy.aquaticcratestesting.config;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.crate.MultiCrate;
import cz.larkyy.aquaticcratestesting.crate.inventories.MultiPreviewGUI;
import cz.larkyy.aquaticcratestesting.utils.colors.Colors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.itemlibrary.CustomItem;
import xyz.larkyy.menulib.Menu;
import xyz.larkyy.menulib.MenuItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MultiCrateConfig extends Config {

    private final String identifier;
    public MultiCrateConfig(JavaPlugin main, File f) {
        super(main, f);
        this.identifier = f.getName().substring(0,f.getName().length()-4);
    }

    public MultiCrate loadCrate() {
        AtomicReference<MultiPreviewGUI> previewGUIAtomicReference = new AtomicReference<>(null);
        MultiCrate c = new MultiCrate(
                identifier,
                Colors.format(getConfiguration().getString("display-name",identifier)),
                getConfiguration().getString("model"),
                loadHologram("hologram"),
                getConfiguration().getDouble("hologram-y-offset",0),
                getConfiguration().getStringList("crates"),
                previewGUIAtomicReference
        );
        loadMultiPreviewGUI(c,previewGUIAtomicReference);

        c.setBlockType(Material.valueOf(getConfiguration().getString("block-type","BARRIER").toUpperCase()));

        return c;
    }
    private List<String> loadHologram(String path) {
        if (!getConfiguration().contains(path)) {
            return new ArrayList<>();
        }
        return getConfiguration().getStringList(path);
    }

    private void loadMultiPreviewGUI(MultiCrate crate, AtomicReference<MultiPreviewGUI> atomicReference) {
        if (!getConfiguration().contains("preview") || !getConfiguration().getBoolean("preview.enabled",true)) {
            return;
        }

        String title;
        if (getConfiguration().contains("preview.title")) {
            title = Colors.format(getConfiguration().getString("preview.title"));
        } else {
            title = crate.getDisplayName()+"§8 Preview";
        }
        Menu.Builder builder =Menu.builder(AquaticCratesTesting.instance())
                .size(getConfiguration().getInt("preview.size",54))
                .title(title);

        if (getConfiguration().contains("preview.items")) {
            for (String str : getConfiguration().getConfigurationSection("preview.items").getKeys(false)) {
                var item = loadMenuItem(str,"preview.items."+str);
                if (item != null) {
                    builder.addItem(item);
                }
            }
        }

        atomicReference.set(new MultiPreviewGUI(crate,
                        builder
                )
        );
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
