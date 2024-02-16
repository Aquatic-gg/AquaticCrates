package cz.larkyy.aquaticcratestesting.commands.impl;

import cz.larkyy.aquaticcratestesting.commands.ICommand;
import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.messages.Messages;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrateCommand implements ICommand {

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return;
        }

        switch (args[1].toLowerCase()) {
            case "give" -> {
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

                Crate crate = Crate.get(args[2]);
                if (crate == null) {
                    Messages.INVALID_CRATE.send(sender);
                    return;
                }

                crate.giveCrate(p);
            }
            case "open" -> {
                if (!sender.hasPermission("aquaticcrates.crate.open")) {
                    Messages.NO_PERMISSION.send(sender);
                    return;
                }

                if (args.length < 5) {
                    return;
                }

                boolean takeKey = true;
                if (args.length > 5) {
                    takeKey = Boolean.parseBoolean(args[5].toLowerCase());
                }

                var instant = Boolean.parseBoolean(args[4].toLowerCase());

                Crate crate = Crate.get(args[2]);
                if (crate == null) {
                    Messages.INVALID_CRATE.send(sender);
                    return;
                }

                Player player = Bukkit.getPlayer(args[3]);
                if (player == null) {
                    Messages.INVALID_PLAYER.send(sender);
                    return;
                }
                crate.open(CratePlayer.get(player), null, instant,takeKey);
            }
        }
    }
}
