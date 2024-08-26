package cz.larkyy.aquaticcrates.commands;

import cz.larkyy.aquaticcrates.commands.impl.*;
import cz.larkyy.aquaticcrates.messages.Messages;
import gg.aquatic.aquaticseries.lib.util.ICommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class Commands implements CommandExecutor, TabCompleter {

    private final Map<String, ICommand> availableCommands = new HashMap<>(){
        {
            put("key",new KeyCommand());
            put("crate",new CrateCommand());
            put("multicrate",new MultiCrateCommand());
            put("item",new ItemCommand());
            put("reload",new ReloadCommand());
            put("milestone",new MilestoneCommand());
        }
    };

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 1) {
            Messages.HELP.send(sender);
            return false;
        }

        ICommand cmd = availableCommands.get(args[0]);
        if (cmd == null) {
            Messages.HELP.send(sender);
            return false;
        }
        cmd.run(sender,args);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 2) {
            return availableCommands.keySet().stream().toList();
        }
        var cmd = availableCommands.get(args[0]);
        if (cmd == null) {
            return new ArrayList<>();
        }
        var args2 = Arrays.stream(args).collect(Collectors.toCollection(ArrayList::new));
        args2.remove(0);
        return cmd.tabComplete(commandSender, args2.toArray(new String[0]));
    }
}
