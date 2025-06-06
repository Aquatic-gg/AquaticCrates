package cz.larkyy.aquaticcrates.commands.impl;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.messages.Messages;
import gg.aquatic.aquaticseries.lib.util.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemCommand implements ICommand {
    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return;
        }

        switch (args[1].toLowerCase()) {
            // item save <Identifier>
            case "save" -> {
                if (!sender.hasPermission("aquaticcrates.item.save")) {
                    Messages.NO_PERMISSION.send(sender);
                    return;
                }

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
                    Messages.MUST_HAVE_ITEM_IN_HAND.send(sender);
                    return;
                }
                if (AquaticCrates.getItemHandler().getItem(id) != null) {
                    Messages.ITEM_UNKNOWN_IDENTIFIER.send(sender);
                    return;
                }

                AquaticCrates.getItemHandler().addItem(id,is);
                Messages.ITEM_SAVED.send(sender);
            }
            // item give <Identifier> [amount] [player]
            case "give" -> {
                if (!sender.hasPermission("aquaticcrates.item.give")) {
                    Messages.NO_PERMISSION.send(sender);
                    return;
                }

                if (args.length < 3) {
                    return;
                }

                String id = args[2];
                ItemStack is = AquaticCrates.getItemHandler().getItem(id);
                if (is == null) {
                    Messages.ITEM_UNKNOWN_IDENTIFIER.send(sender);
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
                    Messages.INVALID_NUMBER.send(sender);
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
                        Messages.INVALID_PLAYER.send(sender);
                        return;
                    }
                    target.getInventory().addItem(is);
                }
            }
        }
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("save","give");
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length == 2) {
                return new ArrayList<>(AquaticCrates.getItemHandler().getItems().keySet());
            }
        }

        return List.of();
    }
}
