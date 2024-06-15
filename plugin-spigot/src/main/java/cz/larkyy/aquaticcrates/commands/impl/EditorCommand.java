package cz.larkyy.aquaticcrates.commands.impl;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.commands.ICommand;
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
