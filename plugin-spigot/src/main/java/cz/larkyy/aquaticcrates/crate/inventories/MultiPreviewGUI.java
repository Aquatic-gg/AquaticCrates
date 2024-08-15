package cz.larkyy.aquaticcrates.crate.inventories;

import cz.larkyy.aquaticcrates.crate.PlacedCrate;
import cz.larkyy.aquaticcrates.crate.PlacedMultiCrate;
import cz.larkyy.aquaticcrates.player.CratePlayer;
import gg.aquatic.aquaticseries.lib.StringExtKt;
import gg.aquatic.aquaticseries.lib.inventory.lib.component.Button;
import gg.aquatic.aquaticseries.lib.inventory.lib.event.ComponentClickEvent;
import gg.aquatic.aquaticseries.lib.inventory.lib.inventory.PersonalizedInventory;
import gg.aquatic.aquaticseries.lib.inventory.lib.title.TitleHolder;
import gg.aquatic.aquaticseries.lib.inventory.lib.title.component.BasicTitleComponent;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholder;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholders;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class MultiPreviewGUI extends PersonalizedInventory {
    private final PlacedMultiCrate multiCrate;

    public MultiPreviewGUI(PlacedMultiCrate multiCrate, @NotNull Player player) {
        super(TitleHolder.Companion.of(
                new BasicTitleComponent(StringExtKt.toAquatic(multiCrate.getMultiCrate().getPreviewSettings().getSettings().getTitle()))
        ), multiCrate.getMultiCrate().getPreviewSettings().getSettings().getSize(), player, factory -> {});
        this.multiCrate = multiCrate;

        addItems();
    }

    private void addItems() {
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
        redrawComponents();
    }

    private void addActionToButton(Button button, Consumer<ComponentClickEvent> consumer) {
        if (button.getOnClick() != null) {
            button.getOnClick().andThen(consumer);
        } else {
            button.setOnClick(consumer);
        }
    }
}
