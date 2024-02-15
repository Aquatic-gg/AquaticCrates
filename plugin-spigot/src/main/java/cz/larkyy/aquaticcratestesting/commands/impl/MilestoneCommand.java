package cz.larkyy.aquaticcratestesting.commands.impl;

import cz.larkyy.aquaticcratestesting.commands.ICommand;
import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class MilestoneCommand implements ICommand {
    // /acrates milestone <crate> set/take/add <player> <amount>
    @Override
    public void run(CommandSender sender, String[] args) {
        if (!sender.hasPermission("aquaticcrates.admin")) {
            Messages.NO_PERMISSION.send(sender);
            return;
        }
        if (args.length < 5) return;
        var crate = Crate.get(args[1]);
        var player = Bukkit.getPlayer(args[3]);
        if (player == null) {
            Messages.INVALID_PLAYER.send(sender);
            return;
        }
        var amount = 1;
        try {
            amount = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            Messages.INVALID_NUMBER.send(sender);
            return;
        }
        if (crate == null) {
            Messages.INVALID_CRATE.send(sender);
            return;
        }
        switch (args[2].toLowerCase()) {
            case "set" -> {
                crate.getMilestoneHandler().setAmt(player,amount);
                sender.sendMessage("Milestone amount has been set to "+player.getName()+"!");
            }
            case "take" -> {
                crate.getMilestoneHandler().decreaseAmt(player,amount);
                sender.sendMessage("Milestone amount has been reduced to "+player.getName()+"!");
            }
            case "add" -> {
                crate.getMilestoneHandler().increaseAmt(player,amount);
                sender.sendMessage("Milestone amount has been increased to "+player.getName()+"!");
            }
        }
    }
}
