package cz.larkyy.aquaticcratestesting.commands.impl;

import cz.larkyy.aquaticcratestesting.commands.ICommand;
import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.crate.Key;
import cz.larkyy.aquaticcratestesting.messages.Messages;
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
            case "give" -> {
                if (!sender.hasPermission("aquaticcrates.key.give")) {
                    Messages.NO_PERMISSION.send(sender);
                    return;
                }

                if (args.length == 2) {
                    return;
                }

                Crate c = Crate.get(args[2]);
                if (c == null) {
                    Messages.KEY_UNKNOWN_IDENTIFIER.send(sender);
                    return;
                }

                if (args.length <= 4) {
                    if (!(sender instanceof Player p)) {
                        Messages.ONLY_FOR_PLAYERS.send(sender);
                        return;
                    }

                    if (args.length == 3) {
                        c.giveKey(p, 1, false);
                        Messages.KEY_GIVE_SENDER
                                .replace("%crate%",c.getIdentifier())
                                .replace("%amount%","1")
                                .replace("%player%",p.getName())
                                .send(sender);
                        Messages.KEY_GIVE_RECEIVER
                                .replace("%crate%",c.getIdentifier())
                                .replace("%amount%","1")
                                .send(p);
                        return;
                    }

                    try {
                        int amount = Integer.parseInt(args[3]);
                        c.giveKey(p,amount,false);
                        Messages.KEY_GIVE_SENDER
                                .replace("%crate%",c.getIdentifier())
                                .replace("%amount%",amount+"")
                                .replace("%player%",p.getName())
                                .send(sender);
                        Messages.KEY_GIVE_RECEIVER
                                .replace("%crate%",c.getIdentifier())
                                .replace("%amount%",amount+"")
                                .send(p);
                    } catch (NumberFormatException e) {
                        Messages.INVALID_NUMBER.send(sender);
                    }
                    return;
                }

                Player target = Bukkit.getPlayer(args[4]);
                if (target == null) {
                    Messages.INVALID_PLAYER.send(sender);
                    return;
                }

                int amount;
                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    Messages.INVALID_NUMBER.send(sender);
                    return;
                }

                if (args.length == 5) {
                    c.giveKey(target,amount,false);
                    Messages.KEY_GIVE_SENDER
                            .replace("%crate%",c.getIdentifier())
                            .replace("%amount%",amount+"")
                            .replace("%player%",target.getName())
                            .send(sender);
                    Messages.KEY_GIVE_RECEIVER
                            .replace("%crate%",c.getIdentifier())
                            .replace("%amount%",amount+"")
                            .send(target);
                }

                if (args.length == 6) {
                    if (args[5].equalsIgnoreCase("virtual")) {
                        c.giveKey(target,amount, true);
                        Messages.KEY_GIVE_SENDER_VIRTUAL
                                .replace("%crate%",c.getIdentifier())
                                .replace("%amount%",amount+"")
                                .replace("%player%",target.getName())
                                .send(sender);
                        Messages.KEY_GIVE_RECEIVER_VIRTUAL
                                .replace("%crate%",c.getIdentifier())
                                .replace("%amount%",amount+"")
                                .send(target);
                    } else {
                        c.giveKey(target,amount, false);
                        Messages.KEY_GIVE_SENDER
                                .replace("%crate%",c.getIdentifier())
                                .replace("%amount%",amount+"")
                                .replace("%player%",target.getName())
                                .send(sender);
                        Messages.KEY_GIVE_RECEIVER
                                .replace("%crate%",c.getIdentifier())
                                .replace("%amount%",amount+"")
                                .send(target);
                    }
                }
            }
            // aquaticcrates key giveall <identifier> [number] [virtual]
            case "giveall" -> {
                if (!sender.hasPermission("aquaticcrates.key.giveall")) {
                    Messages.NO_PERMISSION.send(sender);
                    return;
                }

                if (args.length < 3) {
                    return;
                }
                Crate c = Crate.get(args[2]);
                if (c == null) {
                    Messages.KEY_UNKNOWN_IDENTIFIER.send(sender);
                    return;
                }

                if (args.length == 3) {
                    c.giveKeyAll(1,false);
                    Messages.KEY_GIVE_SENDER
                            .replace("%crate%",c.getIdentifier())
                            .replace("%amount%","1")
                            .replace("%player%","ALL")
                            .send(sender);
                    Messages.KEY_GIVE_RECEIVER
                            .replace("%crate%",c.getIdentifier())
                            .replace("%amount%","1")
                            .broadcast();
                    return;
                }
                int amount = 0;
                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    Messages.INVALID_NUMBER.send(sender);
                }

                if (args.length == 4) {
                    c.giveKeyAll(amount,false);
                    Messages.KEY_GIVE_SENDER
                            .replace("%crate%",c.getIdentifier())
                            .replace("%amount%",amount+"")
                            .replace("%player%","ALL")
                            .send(sender);
                    Messages.KEY_GIVE_RECEIVER
                            .replace("%crate%",c.getIdentifier())
                            .replace("%amount%",amount+"")
                            .broadcast();
                    return;
                }

                if (args[4].equalsIgnoreCase("virtual")) {
                    c.giveKeyAll(amount, true);
                    Messages.KEY_GIVE_SENDER_VIRTUAL
                            .replace("%crate%",c.getIdentifier())
                            .replace("%amount%",amount+"")
                            .replace("%player%","ALL")
                            .send(sender);
                    Messages.KEY_GIVE_RECEIVER_VIRTUAL
                            .replace("%crate%",c.getIdentifier())
                            .replace("%amount%",amount+"")
                            .broadcast();
                } else {
                    c.giveKeyAll(amount, false);
                    Messages.KEY_GIVE_SENDER
                            .replace("%crate%",c.getIdentifier())
                            .replace("%amount%",amount+"")
                            .replace("%player%","ALL")
                            .send(sender);
                    Messages.KEY_GIVE_RECEIVER
                            .replace("%crate%",c.getIdentifier())
                            .replace("%amount%",amount+"")
                            .broadcast();
                }

            }
            // aquaticcrates key take <identifier> <number> <player>
            case "take" -> {
                if (!sender.hasPermission("aquaticcrates.key.take")) {
                    Messages.NO_PERMISSION.send(sender);
                    return;
                }

                if (args.length < 5) {
                    return;
                }
                Crate c = Crate.get(args[2]);
                if (c == null) {
                    Messages.INVALID_CRATE.send(sender);
                    return;
                }

                int amount = 0;
                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    Messages.INVALID_NUMBER.send(sender);
                }

                Player target = Bukkit.getPlayer(args[4]);
                if (target == null) {
                    Messages.INVALID_PLAYER.send(sender);
                    return;
                }

                CratePlayer.get(target).takeKeys(c.getIdentifier(),amount);
                Messages.KEY_TAKE_SENDER
                        .replace("%crate%",c.getIdentifier())
                        .replace("%amount%","1")
                        .replace("%player%","ALL")
                        .send(sender);
                Messages.KEY_TAKE_RECEIVER
                        .replace("%crate%",c.getIdentifier())
                        .replace("%amount%",amount+"")
                        .send(target);
            }
            case "bank" -> {
                if (!sender.hasPermission("aquaticcrates.key.bank")) {
                    Messages.NO_PERMISSION.send(sender);
                    return;
                }

                if (!(sender instanceof Player p)) {
                    return;
                }

                CratePlayer cp = CratePlayer.get(p);
                Messages.KEY_BANK_TITLE.send(p);
                for (Map.Entry<String, Integer> entry : cp.getVirtualKeys().entrySet()) {
                    String s = entry.getKey();
                    Integer i = entry.getValue();
                    Key k = Key.get(s);
                    if (k == null) {
                        continue;
                    }
                    Messages.KEY_BANK_FORMAT
                            .replace("%crate%",s)
                            .replace("%amount%",i+"")
                            .send(p);
                }
            }
        }
    }
}
