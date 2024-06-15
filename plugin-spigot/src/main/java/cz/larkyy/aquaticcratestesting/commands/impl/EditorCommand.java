package cz.larkyy.aquaticcratestesting.commands.impl;

import cz.larkyy.aquaticcratestesting.AquaticCrates;
import cz.larkyy.aquaticcratestesting.commands.ICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditorCommand implements ICommand {
    @Override
    public void run(CommandSender sender, String[] args) {
        if (!sender.hasPermission("aquaticcrates.editor")) {
            return;
        }

        if (!(sender instanceof Player p)) return;
        AquaticCrates.getEditingHandler().openEditorMenu(p);
    }
}
