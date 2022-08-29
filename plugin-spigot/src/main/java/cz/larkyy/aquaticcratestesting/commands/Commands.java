package cz.larkyy.aquaticcratestesting.commands;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.commands.impl.CrateCommand;
import cz.larkyy.aquaticcratestesting.commands.impl.ItemCommand;
import cz.larkyy.aquaticcratestesting.commands.impl.KeyCommand;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Commands implements CommandExecutor {

    private final Map<String, ICommand> availableCommands = new HashMap<>(){
        {
            put("key",new KeyCommand());
            put("crate",new CrateCommand());
            put("item",new ItemCommand());
        }
    };


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 1) {
            return false;
        }

        ICommand cmd = availableCommands.get(args[0]);
        if (cmd == null) {
            return false;
        }
        cmd.run(sender,args);
        return false;
    }
}
