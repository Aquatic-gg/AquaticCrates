package cz.larkyy.aquaticcratestesting.commands;

import cz.larkyy.aquaticcratestesting.crate.Crate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KeyCommand {

    public static void send(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return;
        }

        switch (args[1].toLowerCase()) {
            // /aquaticcrates key give <identifier> [amount] [player] [virtual]
            case "give" -> {
                if (args.length == 2) {
                    return;
                }

                Crate c = Crate.get(args[2]);
                if (c == null) {
                    sender.sendMessage("§cNo Key with this identifier has been found!");
                    return;
                }

                if (args.length <= 4) {
                    if (!(sender instanceof Player p)) {
                        sender.sendMessage("§cYou must be a player to send this command!");
                        return;
                    }
                    if (args.length == 3) {
                        c.giveKey(p, 1, false);
                        return;
                    }

                    try {
                        int amount = Integer.parseInt(args[3]);
                        c.giveKey(p,amount,false);
                    } catch (NumberFormatException e) {
                        sender.sendMessage("§cInvalid number format!");
                    }
                    return;
                }

                Player target = Bukkit.getPlayer(args[4]);
                if (target == null) {
                    sender.sendMessage("§cInvalid player!");
                    return;
                }

                int amount;
                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cInvalid number format!");
                    return;
                }

                if (args.length == 5) {
                    c.giveKey(target,amount,false);
                }

                if (args.length == 6) {
                    if (args[5].equalsIgnoreCase("virtual")) {
                        c.giveKey(target,amount,true);
                    } else {
                        c.giveKey(target,amount,false);
                    }
                }
            }
        }
    }

}
