package cz.larkyy.aquaticcrates.crate;


import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.crate.inventories.MultiPreviewGUI;
import cz.larkyy.aquaticcrates.crate.inventories.settings.MultiPreviewGUISettings;
import cz.larkyy.aquaticcrates.crate.model.ModelSettings;
import cz.larkyy.aquaticcrates.hologram.settings.HologramSettings;
import gg.aquatic.aquaticseries.lib.adapt.AquaticString;
import gg.aquatic.aquaticseries.lib.interactable2.AbstractInteractable;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class MultiCrate extends CrateBase {

    private static final NamespacedKey KEY = new NamespacedKey(AquaticCrates.instance(),"MultiCrateIdentifier");
    private final List<String> crates;
    private final MultiPreviewGUISettings previewSettings;

    public MultiCrate(String identifier, AquaticString displayName, ModelSettings modelSettings, HologramSettings hologram,
                      List<String> crates, int hitboxHeight, int hitboxWidth, MultiPreviewGUISettings previewSettings, AbstractInteractable<?> interactable) {
        super(identifier, displayName, modelSettings, hologram,hitboxHeight,hitboxWidth,interactable);
        this.crates = crates;
        this.previewSettings = previewSettings;
    }

    public List<String> getCrates() {
        return crates;
    }

    public void openPreview(Player p, PlacedMultiCrate pc) {
        if (AquaticCrates.getCrateHandler().isInAnimation(p)) {
            return;
        }
        var gui = new MultiPreviewGUI(pc,p);
        gui.open(true);
    }

    public void giveCrate(Player player) {
        ItemStack is = new ItemStack(Material.CHEST);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§fCrate: "+getIdentifier());
        im.getPersistentDataContainer().set(KEY, PersistentDataType.STRING,getIdentifier());
        is.setItemMeta(im);

        player.getInventory().addItem(is);
    }

    public static MultiCrate get(ItemStack is) {
        if (is == null) return null;
        ItemMeta im = is.getItemMeta();
        if (im == null) return null;

        String id = im.getPersistentDataContainer().get(KEY, PersistentDataType.STRING);
        if (id == null) return null;
        else return get(id);
    }
    public static MultiCrate get(String identifier) {
        return AquaticCrates.getCrateHandler().getMultiCrate(identifier);
    }

    public MultiPreviewGUISettings getPreviewSettings() {
        return previewSettings;
    }
}
