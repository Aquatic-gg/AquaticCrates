package cz.larkyy.aquaticcratestesting.commands;

import cz.larkyy.aquaticcratestesting.commands.impl.*;
import cz.larkyy.aquaticcratestesting.messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Commands implements CommandExecutor {

    private final Map<String, ICommand> availableCommands = new HashMap<>(){
        {
            put("key",new KeyCommand());
            put("crate",new CrateCommand());
            put("multicrate",new MultiCrateCommand());
            put("item",new ItemCommand());
            put("reload",new ReloadCommand());
            put("editor",new EditorCommand());
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
}
