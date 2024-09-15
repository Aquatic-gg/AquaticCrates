package cz.larkyy.aquaticcrates.crate.inventories;

import cz.larkyy.aquaticcrates.crate.PlacedMultiCrate;
import gg.aquatic.aquaticseries.lib.betterinventory2.AquaticInventory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class MultiPreviewGUI extends AquaticInventory {
    private final PlacedMultiCrate multiCrate;

    public MultiPreviewGUI(PlacedMultiCrate multiCrate, @NotNull Player player) {
        super(
                multiCrate.getMultiCrate().getPreviewSettings().getSettings().getTitle(),
                multiCrate.getMultiCrate().getPreviewSettings().getSettings().getSize(),
                multiCrate.getMultiCrate().getPreviewSettings().getSettings().getInventoryType(),
                (p,a) -> {},
                (p,a) -> {},
                (p,a) -> {}
        );
        this.multiCrate = multiCrate;

        addItems();
    }

    private void addItems() {
        /*
        for (var entry : multiCrate.getMultiCrate().getPreviewSettings().getSettings().getButtons().entrySet()) {
            var id = entry.getKey();
            var button = entry.getValue();
            var placeholders = new Placeholders();
            placeholders.addPlaceholder(new Placeholder("%player%", getPlayer().getName()));

            if (id.startsWith("crate-")) {
                String crateId = id.substring(6);
                if (!multiCrate.getPlacedCrates().containsKey(crateId)) continue;
                PlacedCrate pc = multiCrate.getPlacedCrates().get(crateId);

                addActionToButton(button, e -> {
                    if (e.getOriginalEvent().isRightClick()) {
                        pc.getCrate().openPreview(getPlayer(),pc);
                    } else {
                        getPlayer().closeInventory();
                        pc.getCrate().open(CratePlayer.get(getPlayer()),pc,false);
                    }
                    e.getOriginalEvent().setCancelled(true);
                });

                this.getComponentHandler().getComponents().put(id, button);
            } else if (id.equals("close-button")) {
                addActionToButton(button, e -> {
                    getPlayer().closeInventory();
                    e.getOriginalEvent().setCancelled(true);
                });

                this.getComponentHandler().getComponents().put(id, button);
            } else {
                this.getComponentHandler().getComponents().put(id, button);
            }
        }
        if (multiCrate.getMultiCrate().getPreviewSettings().isClearBottomInventory()) {
            for (int i = 0; i < multiCrate.getMultiCrate().getPreviewSettings().getSettings().getSize() + 36 - 1; i++) {
                var button = new Button(
                        new ItemStack(Material.AIR),
                        SlotSelection.Companion.of(i),
                        e -> {
                            e.getOriginalEvent().setCancelled(true);
                        },
                        0
                );
                getComponentHandler().getComponents().put("clear-item-" + i, button);
            }
        }

        redrawComponents();

         */
    }
}
