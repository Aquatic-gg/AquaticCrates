package cz.larkyy.aquaticcratestesting.commands.impl;

import cz.larkyy.aquaticcratestesting.AquaticCrates;
import cz.larkyy.aquaticcratestesting.commands.ICommand;
import cz.larkyy.aquaticcratestesting.messages.Messages;
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
