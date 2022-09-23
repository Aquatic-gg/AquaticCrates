package cz.larkyy.aquaticcratestesting.editor.datatype;

import org.bukkit.entity.Player;

public interface DataType {

    public void input(Player p);

    public boolean validate();
}
