package cz.larkyy.aquaticcrates.commands.impl;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.crate.MultiCrate;
import cz.larkyy.aquaticcrates.messages.Messages;
import gg.aquatic.aquaticseries.lib.util.ICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("give");
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length == 2) {
                return new ArrayList<>(AquaticCrates.getCrateHandler().getCrates().keySet());
            }
        }

        return List.of();
    }
}
