package cz.larkyy.aquaticcratestesting.hooks;

import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.crate.milestone.Milestone;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PAPIHook extends PlaceholderExpansion {
    @Override
    public String getAuthor() {
        return "Larkyy";
    }

    @Override
    public String getIdentifier() {
        return "aquaticcrates";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("inanimation")) {
            if (!player.isOnline()) {
                return null;
            }
            Player p = (Player) player;
            CratePlayer cp = CratePlayer.get(p);
            return cp.isInAnimation()+"";
        }
        String[] args = params.split("_");
        switch (args[0].toLowerCase()) {
            case "keys" -> {
                if (!player.isOnline()) {
                    return null;
                }
                if (args.length < 2) {
                    return null;
                }
                CratePlayer cp = CratePlayer.get(player.getPlayer());
                return cp.getKeys(args[1])+"";
            }
            case "milestone" -> {

                if (!player.isOnline()) {
                    return null;
                }
                Player p = (Player) player;

                if (args.length < 4) {
                    return "";
                }
                var crate = Crate.get(args[2]);
                if (crate == null) return "";

                if (args[1].equalsIgnoreCase("isreached")) {
                    Milestone milestone = crate.getMilestoneHandler().getMilestones().get(Integer.valueOf(args[3]));
                    if (milestone == null) return "";
                    return (milestone.getMilestone()<=crate.getMilestoneHandler().getAmt(p))+"";
                }

                if (args[1].equalsIgnoreCase("reached")) {
                    Milestone milestone = crate.getMilestoneHandler().getMilestones().get(Integer.valueOf(args[3]));
                    if (milestone == null) {
                        milestone = crate.getMilestoneHandler().getRepeatableMilestones().get(Integer.valueOf(args[3]));
                    }
                    if (milestone == null) return "";
                    return crate.getMilestoneHandler().getAmt(p)+"";
                }
                if (args[1].equalsIgnoreCase("remaining")) {
                    Milestone milestone = crate.getMilestoneHandler().getMilestones().get(Integer.valueOf(args[3]));
                    if (milestone == null) {
                        milestone = crate.getMilestoneHandler().getRepeatableMilestones().get(Integer.valueOf(args[3]));
                    }
                    if (milestone == null) return "";
                    return (milestone.getMilestone()-crate.getMilestoneHandler().getAmt(p))+"";
                }
                if (args[1].equalsIgnoreCase("required")) {
                    Milestone milestone = crate.getMilestoneHandler().getMilestones().get(Integer.valueOf(args[3]));
                    if (milestone == null) {
                        milestone = crate.getMilestoneHandler().getRepeatableMilestones().get(Integer.valueOf(args[3]));
                    }
                    if (milestone == null) return "";
                    return milestone.getMilestone()+"";
                }
            } case "repeatable-milestone" -> {

                if (!player.isOnline()) {
                    return null;
                }
                Player p = (Player) player;

                if (args.length < 4) {
                    return "";
                }
                var crate = Crate.get(args[2]);
                if (crate == null) return "";
                Milestone milestone = crate.getMilestoneHandler().getRepeatableMilestones().get(Integer.valueOf(args[3]));
                if (milestone == null) return "";

                var current = crate.getMilestoneHandler().getAmt(p);

                double d1 = (double)current/(double)milestone.getMilestone();
                double d2 = Math.floor(d1);

                int reached = (int) ((d1-d2)*milestone.getMilestone());

                if (args[1].equalsIgnoreCase("reached")) {
                    return reached + "";
                }
                if (args[1].equalsIgnoreCase("remaining")) {
                    return (milestone.getMilestone() - reached) + "";
                }
                if (args[1].equalsIgnoreCase("required")) {
                    return milestone.getMilestone()+"";
                }
            }
        }

        return null;
    }
}
