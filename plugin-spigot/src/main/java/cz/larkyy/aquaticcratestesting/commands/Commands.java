package cz.larkyy.aquaticcratestesting.commands;

import cz.larkyy.aquaticcratestesting.commands.impl.CrateCommand;
import cz.larkyy.aquaticcratestesting.commands.impl.ItemCommand;
import cz.larkyy.aquaticcratestesting.commands.impl.KeyCommand;
import cz.larkyy.aquaticcratestesting.commands.impl.ReloadCommand;
import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.editor.Editor;
import cz.larkyy.aquaticcratestesting.messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Commands implements CommandExecutor {

    private final Map<String, ICommand> availableCommands = new HashMap<>(){
        {
            put("key",new KeyCommand());
            put("crate",new CrateCommand());
            put("item",new ItemCommand());
            put("reload",new ReloadCommand());
        }
    };

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 1) {
            Messages.HELP.send(sender);
            return false;
        }

        if (args.length > 1 && args[0].equalsIgnoreCase("editor")) {
            new Editor(
                    null,
                    (Player) sender,
                    Crate.class,
                    Crate.get(args[1]),
                    "Crate Editor"
            ).open((Player) sender);
            return true;
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
