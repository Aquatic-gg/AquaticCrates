package cz.larkyy.aquaticcrates.commands.impl;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.crate.Crate;
import cz.larkyy.aquaticcrates.messages.Messages;
import gg.aquatic.aquaticseries.lib.util.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(AquaticCrates.getCrateHandler().getCrates().keySet());
        }
        if (args.length == 2) {
            return Arrays.asList("set","take","add");
        }

        return List.of();
    }
}
