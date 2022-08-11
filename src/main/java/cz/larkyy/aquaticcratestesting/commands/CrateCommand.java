package cz.larkyy.aquaticcratestesting.commands;

import cz.larkyy.aquaticcratestesting.crate.Crate;
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
                if (args.length == 2) {
                    return;
                }

                if (!(sender instanceof Player p)) {
                    sender.sendMessage("§cThis command is for players only!");
                    return;
                }

                Crate crate = Crate.get(args[2]);
                if (crate == null) {
                    sender.sendMessage("§cNo crate with this identifier has been found!");
                    return;
                }

                crate.giveCrate(p);
            }
        }
    }
}
