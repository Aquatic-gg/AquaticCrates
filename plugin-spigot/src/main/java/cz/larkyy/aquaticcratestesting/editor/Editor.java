package cz.larkyy.aquaticcratestesting.editor;

import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.editor.datatype.DataType;
import cz.larkyy.aquaticcratestesting.editor.datatype.impl.IntDataType;
import cz.larkyy.aquaticcratestesting.editor.datatype.impl.StringDataType;
import org.bukkit.Bukkit;

import java.util.List;

public class Editor {

    public enum Page {
        MAIN,
        KEY,
        REWARDS,
        REWARD
    }

    public enum FieldType {
        STRING(new StringDataType()),
        INT(new IntDataType());

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

    public void run() {
        List<EditorItem> items = EditorUtils.getItems(crate);
        items.forEach(i -> {
            Bukkit.broadcastMessage("Category: "+i.page()+" | Type: "+i.type()+" | Field: "+i.field());
        });
    }
}
