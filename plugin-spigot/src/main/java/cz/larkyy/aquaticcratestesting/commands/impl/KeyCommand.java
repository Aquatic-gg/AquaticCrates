package cz.larkyy.aquaticcratestesting.commands.impl;

import cz.larkyy.aquaticcratestesting.commands.ICommand;
import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.crate.Key;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class KeyCommand implements ICommand {

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return;
        }

        switch (args[1].toLowerCase()) {
            // /aquaticcrates key give <identifier> [amount] [player] [virtual]
            case "give": {
                if (args.length == 2) {
                    return;
                }

                Crate c = Crate.get(args[2]);
                if (c == null) {
                    sender.sendMessage("§cNo Key with this identifier has been found!");
                    return;
                }

                if (args.length <= 4) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("§cYou must be a player to send this command!");
                        return;
                    }
                    Player p = (Player) sender;

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
                    c.giveKey(target,amount, args[5].equalsIgnoreCase("virtual"));
                }
            }
            // aquaticcrates key giveall <identifier> [number] [virtual]
            case "giveall": {
                if (args.length < 3) {
                    return;
                }
                Crate c = Crate.get(args[2]);
                if (c == null) {
                    sender.sendMessage("§cNo Key with this identifier has been found!");
                    return;
                }

                if (args.length == 3) {
                    c.giveKeyAll(1,false);
                    return;
                }
                int amount = 0;
                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cInvalid number format!");
                }

                if (args.length == 4) {
                    c.giveKeyAll(amount,false);
                    return;
                }

                c.giveKeyAll(amount, args[4].equalsIgnoreCase("virtual"));

            }
            // aquaticcrates key take <identifier> <number> <player>
            case "take": {
                if (args.length < 5) {
                    return;
                }
                Crate c = Crate.get(args[2]);
                if (c == null) {
                    sender.sendMessage("§cNo Key with this identifier has been found!");
                    return;
                }

                int amount = 0;
                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cInvalid number format!");
                }

                Player target = Bukkit.getPlayer(args[4]);
                if (target == null) {
                    sender.sendMessage("§cInvalid player!");
                    return;
                }

                CratePlayer.get(target).takeKeys(c.getIdentifier(),amount);
                target.sendMessage("You have been taken "+amount+"x "+args[2]+" Key!");
            }
            case "bank": {
                if (!(sender instanceof Player)) {
                    return;
                }
                Player p = (Player) sender;

                CratePlayer cp = CratePlayer.get(p);
                p.sendMessage("Your Virtual Keys:");
                for (Map.Entry<String, Integer> entry : cp.getVirtualKeys().entrySet()) {
                    String s = entry.getKey();
                    Integer i = entry.getValue();
                    Key k = Key.get(s);
                    if (k == null) {
                        continue;
                    }
                    p.sendMessage(s+" Key: "+i);
                }
            }
        }
    }
}
