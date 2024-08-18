package cz.larkyy.aquaticcrates.commands.impl;

import cz.larkyy.aquaticcrates.AquaticCrates;
import gg.aquatic.aquaticseries.lib.util.ICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EditorCommand implements ICommand {
    @Override
    public void run(CommandSender sender, String[] args) {
        if (!sender.hasPermission("aquaticcrates.editor")) {
            return;
        }

        if (!(sender instanceof Player p)) return;
        AquaticCrates.getEditingHandler().openEditorMenu(p);
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        return List.of();
    }
}
