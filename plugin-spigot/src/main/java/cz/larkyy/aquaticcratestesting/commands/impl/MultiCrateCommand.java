package cz.larkyy.aquaticcratestesting.commands.impl;

import cz.larkyy.aquaticcratestesting.commands.ICommand;
import cz.larkyy.aquaticcratestesting.crate.MultiCrate;
import cz.larkyy.aquaticcratestesting.messages.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MultiCrateCommand implements ICommand {
    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return;
        }

        if (args[1].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("aquaticcrates.crate.give")) {
                Messages.NO_PERMISSION.send(sender);
                return;
            }

            if (args.length == 2) {
                return;
            }

            if (!(sender instanceof Player p)) {
                Messages.ONLY_FOR_PLAYERS.send(sender);
                return;
            }

            MultiCrate crate = MultiCrate.get(args[2]);
            if (crate == null) {
                Messages.INVALID_CRATE.send(sender);
                return;
            }

            crate.giveCrate(p);
        }
    }
}
