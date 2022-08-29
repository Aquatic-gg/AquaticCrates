package cz.larkyy.aquaticcratestesting.commands.impl;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.commands.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCommand implements ICommand {
    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return;
        }

        switch (args[1].toLowerCase()) {
            // item save <Identifier>
            case "save": {
                if (!(sender instanceof Player)) {
                    return;
                }
                Player p = (Player) sender;

                if (args.length < 3) {
                    return;
                }

                String id = args[2];
                ItemStack is = p.getInventory().getItemInMainHand();
                if (is.getType() == Material.AIR) {
                    p.sendMessage("§cYou must have an item in your hand!");
                    return;
                }
                if (AquaticCratesTesting.getItemHandler().getItem(id) != null) {
                    p.sendMessage("§cThere's already saved an item with this identifier!");
                    return;
                }

                AquaticCratesTesting.getItemHandler().addItem(id,is);
                p.sendMessage("§aItem saved!");
            }
            // item give <Identifier> [amount] [player]
            case "give": {
                if (args.length < 3) {
                    return;
                }

                String id = args[2];
                ItemStack is = AquaticCratesTesting.getItemHandler().getItem(id);
                if (is == null) {
                    sender.sendMessage("Unknown identifier!");
                    return;
                }
                is.setAmount(1);

                if (args.length == 3) {
                    if (!(sender instanceof Player)) {
                        return;
                    }
                    Player p = (Player) sender;
                    p.getInventory().addItem(is);
                    return;
                }

                int amount = 1;
                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cInvalid number format!");
                    return;
                }
                is.setAmount(amount);

                if (args.length == 4) {
                    if (!(sender instanceof Player)) {
                        return;
                    }
                    Player p = (Player) sender;
                    p.getInventory().addItem(is);
                }
                else {
                    Player target = Bukkit.getPlayer(args[4]);
                    if (target == null) {
                        sender.sendMessage("Unknown player");
                        return;
                    }
                    target.getInventory().addItem(is);
                }
            }
        }
    }
}
