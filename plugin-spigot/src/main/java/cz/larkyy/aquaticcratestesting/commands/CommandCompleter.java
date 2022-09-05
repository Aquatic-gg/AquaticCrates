package cz.larkyy.aquaticcratestesting.commands;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandCompleter implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("aquaticcrates")) {
            return null;
        }

        if (args.length <= 1) {
            return Arrays.asList("key","crate","item");
        }
        else if (args[0].equalsIgnoreCase("key")) {
            if (args.length == 2) {
                return Arrays.asList("give","giveall","take");
            }
            if (args[1].equalsIgnoreCase("give")) {
                if (args.length == 3) {
                    return new ArrayList<>(AquaticCratesTesting.getCrateHandler().getCrates().keySet());
                }
            } else if (args[1].equalsIgnoreCase("giveall")) {
                if (args.length == 3) {
                    return new ArrayList<>(AquaticCratesTesting.getCrateHandler().getCrates().keySet());
                }
            } else if (args[1].equalsIgnoreCase("take")) {
                if (args.length == 3) {
                    return new ArrayList<>(AquaticCratesTesting.getCrateHandler().getCrates().keySet());
                }
            }

            return null;
        }
        else if (args[0].equalsIgnoreCase("crate")) {
            if (args.length == 2) {
                return Arrays.asList("give");
            }
            if (args[1].equalsIgnoreCase("give")) {
                if (args.length == 3) {
                    return new ArrayList<>(AquaticCratesTesting.getCrateHandler().getCrates().keySet());
                }
            }

            return null;
        }
        else if (args[0].equalsIgnoreCase("item")) {
            if (args.length == 2) {
                return Arrays.asList("save","give");
            }
            if (args[1].equalsIgnoreCase("give")) {
                if (args.length == 3) {
                    return new ArrayList<>(AquaticCratesTesting.getItemHandler().getItems().keySet());
                }
            }
        }
        return null;
    }
}
