package cz.larkyy.aquaticcratestesting.crate.reward;

import cz.larkyy.aquaticcratestesting.crate.reward.actions.ActionBarAction;
import cz.larkyy.aquaticcratestesting.crate.reward.actions.CommandAction;
import cz.larkyy.aquaticcratestesting.crate.reward.actions.MessageAction;
import cz.larkyy.aquaticcratestesting.crate.reward.actions.TitleAction;
import org.bukkit.entity.Player;
import xyz.larkyy.colorutils.Colors;

public abstract class RewardAction {

    public abstract void run(Player player);

    public static RewardAction get(String str) {
        String args;
        if (str.startsWith("[message]")) {
            args = str.substring(9).trim();
            return new MessageAction(args);
        } else if (str.startsWith("[command]")) {
            args = str.substring(9).trim();
            return new CommandAction(args);
        } else if (str.startsWith("[actionbar]")) {
            args = str.substring(11).trim();
            return new ActionBarAction(args);
        } else if (str.startsWith("[title]")) {
            args = Colors.format(str.substring(7).trim());

            String[] arguments = args.split("-\\|\\|-");
            String[] lines = arguments[0].split("\\n");

            int in = 10;
            int stay = 40;
            int out = 10;

            if (arguments.length > 1) {
                String[] titleValues = arguments[1].split(";");
                in = Integer.parseInt(titleValues[0]);
                stay = Integer.parseInt(titleValues[1]);
                out = Integer.parseInt(titleValues[2]);
            }

            return new TitleAction(lines[0], (lines.length == 1 ? "" : lines[1]),in,stay,out);
        }
        return null;
    }
}
