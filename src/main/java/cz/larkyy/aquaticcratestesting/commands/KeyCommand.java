package cz.larkyy.aquaticcratestesting.commands;

import cz.larkyy.aquaticcratestesting.crate.Crate;
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

                if (args.length == 3) {
                    if (!(sender instanceof Player p)) {
                        sender.sendMessage("§cYou must be a player to send this command!");
                        return;
                    }
                    c.giveKey(p,1,false);
                    return;
                }

                if (args.length == 4) {

                }
            }
        }
    }

}
