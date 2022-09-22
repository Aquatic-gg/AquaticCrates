package cz.larkyy.aquaticcratestesting.editor;

import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.editor.datatype.DataType;
import cz.larkyy.aquaticcratestesting.editor.datatype.impl.IntDataType;
import cz.larkyy.aquaticcratestesting.editor.datatype.impl.ObjectListDataType;
import cz.larkyy.aquaticcratestesting.editor.datatype.impl.StringDataType;
import cz.larkyy.aquaticcratestesting.editor.datatype.impl.StringListDataType;
import cz.larkyy.aquaticcratestesting.editor.menus.EditorMenu;
import cz.larkyy.aquaticcratestesting.editor.menus.impl.BasicEditorMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class Editor {

    public static final List<Page> CATEGORIES = new ArrayList<>(Arrays.asList(
            Page.MAIN,
            Page.KEY,
            Page.REWARD,
            Page.REWARDS
    ));

    public enum Page {
        MAIN(new BasicEditorMenu("Crates Editor")),
        KEY(new BasicEditorMenu("Key Editor")),
        REWARDS(new BasicEditorMenu("Rewards Editor")),
        REWARD(new BasicEditorMenu("Reward Editor"));

        private final EditorMenu menu;

        Page(EditorMenu menu) {
            this.menu = menu;
        }

        public EditorMenu menu() {
            return menu;
        }
    }

    public enum FieldType {
        STRING(new StringDataType()),
        INT(new IntDataType()),
        STRING_LIST(new StringListDataType()),
        OBJECT_LIST(new ObjectListDataType());

        private final DataType dataType;
        FieldType(DataType dataType) {
            this.dataType = dataType;
        }

        public DataType get() {
            return dataType;
        }
    }

    private final Crate crate;

    public Editor(Crate crate) {
        this.crate = crate;
    }

    public void build(Player p) {
        List<EditorItem> items = EditorUtils.getItems(crate);

        Map<Page,EditorMenu> editorMenus = new HashMap<>();
        for (Page page : CATEGORIES) {
            editorMenus.put(page,page.menu);
        }
        for (EditorItem item : items) {
            Bukkit.broadcastMessage("Loading item for "+item.page().toString());
            Bukkit.broadcastMessage("  ID: "+item.getId());
            Bukkit.broadcastMessage("  Slot: "+item.getSlots().get(0));
            editorMenus.get(item.page()).addEditorItem(item);
        }

        editorMenus.get(Page.MAIN).open(p);
    }
}
