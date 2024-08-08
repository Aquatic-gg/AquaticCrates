package cz.larkyy.aquaticcrates.crate;


import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.crate.inventories.MultiPreviewGUI;
import cz.larkyy.aquaticcrates.crate.model.ModelSettings;
import gg.aquatic.aquaticseries.lib.adapt.AquaticString;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MultiCrate extends CrateBase {

    private static final NamespacedKey KEY = new NamespacedKey(AquaticCrates.instance(),"MultiCrateIdentifier");
    private final List<String> crates;
    private final AtomicReference<MultiPreviewGUI> previewGUI;

    public MultiCrate(String identifier, AquaticString displayName, ModelSettings modelSettings, List<String> hologram, double hologramYOffset,
                      List<String> crates, AtomicReference<MultiPreviewGUI> previewGUI, int hitboxHeight, int hitboxWidth) {
        super(identifier, displayName, modelSettings, hologram, hologramYOffset,hitboxHeight,hitboxWidth);
        this.crates = crates;
        this.previewGUI = previewGUI;
    }

    public List<String> getCrates() {
        return crates;
    }

    public void openPreview(Player p, PlacedMultiCrate pc) {
        if (AquaticCrates.getCrateHandler().isInAnimation(p)) {
            return;
        }
        MultiPreviewGUI gui = previewGUI.get();
        if (gui == null) {
            return;
        }
        gui.open(p, pc);
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
}
