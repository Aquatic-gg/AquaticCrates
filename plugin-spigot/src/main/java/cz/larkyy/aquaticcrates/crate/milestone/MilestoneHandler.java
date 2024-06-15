package cz.larkyy.aquaticcrates.crate.milestone;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.crate.Crate;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.TreeMap;

public class MilestoneHandler {

    private final TreeMap<Integer,Milestone> milestones;
    private final HashMap<Integer,Milestone> repeatableMilestones;
    private final NamespacedKey namespacedKey;

    public MilestoneHandler(Crate crate, TreeMap<Integer,Milestone> milestones, HashMap<Integer,Milestone> repeatableMilestones) {
        this.milestones = milestones;
        this.repeatableMilestones = repeatableMilestones;
        this.namespacedKey = new NamespacedKey(AquaticCrates.instance(),crate.getIdentifier()+"-milestone");
    }

    public TreeMap<Integer, Milestone> getMilestones() {
        return milestones;
    }

    public HashMap<Integer, Milestone> getRepeatableMilestones() {
        return repeatableMilestones;
    }

    public Milestone getNext(Player player) {
        var amt = getAmt(player);
        var milestone = milestones.higherEntry(amt);
        if (milestone == null) return null;
        return milestone.getValue();
    }

    public int requiredToReach(Player player) {
        var amt = getAmt(player);
        var milestone = milestones.higherEntry(amt);
        if (milestone == null) return -1;
        return milestone.getKey()-amt;
    }

    public void increaseAmt(Player player, int amount) {
        var amt = getAmt(player)+amount;
        if (amt < 0) amt = 0;
        var pdc = player.getPersistentDataContainer();
        pdc.set(namespacedKey,PersistentDataType.INTEGER,amt);

        if (amt == 0) return;
        checkMilestones(player, amt);
    }

    public void increaseAmt(Player player) {
        increaseAmt(player,1);
    }

    public void decreaseAmt(Player player, int amount) {
        increaseAmt(player,-amount);
    }

    public void setAmt(Player player, int amount) {
        var amt = amount;
        if (amt < 0) amt = 0;
        var pdc = player.getPersistentDataContainer();
        pdc.set(namespacedKey,PersistentDataType.INTEGER,amt);

        if (amt == 0) return;
        checkMilestones(player, amt);
    }

    public int getAmt(Player player) {
        var pdc = player.getPersistentDataContainer();
        var val = pdc.get(namespacedKey, PersistentDataType.INTEGER);
        if (val == null) return 0;
        return val;
    }

    public void checkMilestones(Player player, int amt) {
        if (milestones.containsKey(amt)) {
            var milestone = milestones.get(amt);
            milestone.giveRandomReward(player);
        }

        for (Milestone m : repeatableMilestones.values()) {
            if ((double)amt % m.getMilestone()  == 0) {
                m.giveRandomReward(player);
            }
        }
    }
}
