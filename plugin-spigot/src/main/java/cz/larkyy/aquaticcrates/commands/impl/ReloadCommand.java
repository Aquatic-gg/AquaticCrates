package cz.larkyy.aquaticcrates.commands.impl;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.commands.ICommand;
import cz.larkyy.aquaticcrates.messages.Messages;
import org.bukkit.command.CommandSender;

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
}
