package cz.larkyy.aquaticcrates.commands.impl;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.messages.Messages;
import gg.aquatic.aquaticseries.lib.util.ICommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReloadCommand implements ICommand {
    @Override
    public void run(CommandSender sender, String[] args) {
        if (!sender.hasPermission("aquaticcrates.reload")) {
            Messages.NO_PERMISSION.send(sender);
            return;
        }
        Messages.PLUGIN_RELOADED.send(sender);
        AquaticCrates.instance().reload();
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        return List.of();
    }
}
