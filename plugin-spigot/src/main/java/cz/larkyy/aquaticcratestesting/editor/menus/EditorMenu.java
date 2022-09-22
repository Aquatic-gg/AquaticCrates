package cz.larkyy.aquaticcratestesting.editor.menus;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.editor.Editor;
import cz.larkyy.aquaticcratestesting.item.impl.VanillaItem;
import org.bukkit.Material;
import xyz.larkyy.colorutils.Colors;
import xyz.larkyy.menulib.Menu;
import xyz.larkyy.menulib.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;

public class EditorMenu {

    private final Menu.Builder menuBuilder;
    private final Editor editor;

    public EditorMenu(String title, Editor editor) {
        this.editor = editor;
        menuBuilder = Menu.builder(AquaticCratesTesting.instance())
                .title(Colors.format(title))
                        .size(27);
        prepareMenu();
    }

    private void prepareMenu() {
        MenuItem.Builder itemBuilder = MenuItem.builder(
                "fill",
                new VanillaItem(
                        Material.BLACK_STAINED_GLASS_PANE,
                        " ",
                        new ArrayList<>(),
                        1,
                        -1
                ).getItem()
        );

        if (editor.getPreviousEditor() == null) {
            itemBuilder.slots(Arrays.asList(0,1,2,3,4,5,6,7,8,
                    18,19,20,21,22,23,24,25,26));
        } else {
            itemBuilder.slots(Arrays.asList(0,1,2,3,4,5,6,7,8,
                    18,19,20,21,23,24,25,26));

            menuBuilder.addItem(
                    MenuItem.builder("back",
                            new VanillaItem(
                                    Material.ARROW,
                                    "§cBack",
                                    new ArrayList<>(),
                                    1,
                                    -1
                            ).getItem())
                            .slots(Arrays.asList(22))
                            .action(e -> {
                                if (e.isLeftClick()) {
                                    editor.getPreviousEditor().open(editor.getPlayer());
                                }
                            })
                            .build()
            );
        }

        menuBuilder.addItem(itemBuilder.build());
    }

    public void addItem(MenuItem menuItem) {
        menuBuilder.addItem(menuItem);
    }

    public Menu build() {
        return menuBuilder.build();
    }
}
