package cz.larkyy.aquaticcratestesting.editor.datatype.impl;

import cz.larkyy.aquaticcratestesting.editor.datatype.DataType;
import org.bukkit.entity.Player;

public class StringListDataType implements DataType {
    @Override
    public void input(Player p) {

    }

    @Override
    public boolean validate() {
        return false;
    }
}
