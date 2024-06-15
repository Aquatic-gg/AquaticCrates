package cz.larkyy.aquaticcrates.commands.impl;

import cz.larkyy.aquaticcrates.commands.ICommand;
import cz.larkyy.aquaticcrates.crate.Crate;
import cz.larkyy.aquaticcrates.crate.Key;
import cz.larkyy.aquaticcrates.messages.Messages;
import cz.larkyy.aquaticcrates.player.CratePlayer;
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

                String keyName = c.getKey().getItem(1).getItemMeta().getDisplayName();
                if (args.length <= 4) {
                    if (!(sender instanceof Player p)) {
                        Messages.ONLY_FOR_PLAYERS.send(sender);
                        return;
                    }

                    if (args.length == 3) {
                        c.giveKey(p, 1, false);
                        Messages.KEY_GIVE_SENDER
                                .replace("%crate%",c.getDisplayName())
                                .replace("%amount%","1")
                                .replace("%player%",p.getName())
                                .replace("%key_name%",keyName)
                                .send(sender);
                        Messages.KEY_GIVE_RECEIVER
                                .replace("%crate%",c.getDisplayName())
                                .replace("%amount%","1")
                                .replace("%key_name%",keyName)
                                .send(p);
                        return;
                    }

                    try {
                        int amount = Integer.parseInt(args[3]);
                        c.giveKey(p,amount,false);
                        Messages.KEY_GIVE_SENDER
                                .replace("%crate%",c.getDisplayName())
                                .replace("%amount%",amount+"")
                                .replace("%player%",p.getName())
                                .replace("%key_name%",keyName)
                                .send(sender);
                        Messages.KEY_GIVE_RECEIVER
                                .replace("%crate%",c.getDisplayName())
                                .replace("%amount%",amount+"")
                                .replace("%key_name%",keyName)
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
                            .replace("%crate%",c.getDisplayName())
                            .replace("%amount%",amount+"")
                            .replace("%player%",target.getName())
                            .replace("%key_name%",keyName)
                            .send(sender);
                    Messages.KEY_GIVE_RECEIVER
                            .replace("%crate%",c.getDisplayName())
                            .replace("%amount%",amount+"")
                            .replace("%key_name%",keyName)
                            .send(target);
                }

                if (args.length == 6) {
                    if (args[5].equalsIgnoreCase("virtual")) {
                        c.giveKey(target,amount, true);
                        Messages.KEY_GIVE_SENDER_VIRTUAL
                                .replace("%crate%",c.getDisplayName())
                                .replace("%amount%",amount+"")
                                .replace("%player%",target.getName())
                                .replace("%key_name%",keyName)
                                .send(sender);
                        Messages.KEY_GIVE_RECEIVER_VIRTUAL
                                .replace("%crate%",c.getDisplayName())
                                .replace("%amount%",amount+"")
                                .replace("%key_name%",keyName)
                                .send(target);
                    } else {
                        c.giveKey(target,amount, false);
                        Messages.KEY_GIVE_SENDER
                                .replace("%crate%",c.getDisplayName())
                                .replace("%amount%",amount+"")
                                .replace("%player%",target.getName())
                                .replace("%key_name%",keyName)
                                .send(sender);
                        Messages.KEY_GIVE_RECEIVER
                                .replace("%crate%",c.getDisplayName())
                                .replace("%amount%",amount+"")
                                .replace("%key_name%",keyName)
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
                String keyName = c.getKey().getItem(1).getItemMeta().getDisplayName();

                if (args.length == 3) {
                    c.giveKeyAll(1,false);
                    Messages.KEY_GIVE_SENDER
                            .replace("%crate%",c.getDisplayName())
                            .replace("%amount%","1")
                            .replace("%player%","ALL")
                            .replace("%key_name%",keyName)
                            .send(sender);
                    Messages.KEY_GIVE_RECEIVER
                            .replace("%crate%",c.getDisplayName())
                            .replace("%amount%","1")
                            .replace("%key_name%",keyName)
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
                            .replace("%crate%",c.getDisplayName())
                            .replace("%amount%",amount+"")
                            .replace("%player%","ALL")
                            .replace("%key_name%",keyName)
                            .send(sender);
                    Messages.KEY_GIVE_RECEIVER
                            .replace("%crate%",c.getDisplayName())
                            .replace("%amount%",amount+"")
                            .replace("%key_name%",keyName)
                            .broadcast();
                    return;
                }

                if (args[4].equalsIgnoreCase("virtual")) {
                    c.giveKeyAll(amount, true);
                    Messages.KEY_GIVE_SENDER_VIRTUAL
                            .replace("%crate%",c.getDisplayName())
                            .replace("%amount%",amount+"")
                            .replace("%player%","ALL")
                            .replace("%key_name%",keyName)
                            .send(sender);
                    Messages.KEY_GIVE_RECEIVER_VIRTUAL
                            .replace("%crate%",c.getDisplayName())
                            .replace("%amount%",amount+"")
                            .replace("%key_name%",keyName)
                            .broadcast();
                } else {
                    c.giveKeyAll(amount, false);
                    Messages.KEY_GIVE_SENDER
                            .replace("%crate%",c.getDisplayName())
                            .replace("%amount%",amount+"")
                            .replace("%player%","ALL")
                            .replace("%key_name%",keyName)
                            .send(sender);
                    Messages.KEY_GIVE_RECEIVER
                            .replace("%crate%",c.getDisplayName())
                            .replace("%amount%",amount+"")
                            .replace("%key_name%",keyName)
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
                String keyName = c.getKey().getItem(1).getItemMeta().getDisplayName();

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
                        .replace("%crate%",c.getDisplayName())
                        .replace("%amount%",amount+"")
                        .replace("%player%",target.getName())
                        .replace("%key_name%",keyName)
                        .send(sender);
                Messages.KEY_TAKE_RECEIVER
                        .replace("%crate%",c.getDisplayName())
                        .replace("%amount%",amount+"")
                        .replace("%key_name%",keyName)
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
                    String keyName = k.getItem(1).getItemMeta().getDisplayName();
                    Messages.KEY_BANK_FORMAT
                            .replace("%crate%",k.getDisplayName())
                            .replace("%amount%",i+"")
                            .replace("%key_name%",keyName)
                            .send(p);
                }
            }
        }
    }
}
