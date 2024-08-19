package cz.larkyy.aquaticcrates.crate.milestone;

import cz.larkyy.aquaticcrates.utils.RewardUtils;
import gg.aquatic.aquaticseries.lib.adapt.AquaticString;
import gg.aquatic.aquaticseries.lib.chance.IChance;
import org.bukkit.entity.Player;

import java.util.List;

public class Milestone {

    private final int milestone;
    private final AquaticString displayName;
    private final List<IChance> rewards;

    public Milestone(int milestone, List<IChance> rewards, AquaticString displayName) {
        this.milestone = milestone;
        this.rewards = rewards;
        this.displayName = displayName;
    }

    public AquaticString getDisplayName() {
        return displayName;
    }

    public int getMilestone() {
        return milestone;
    }

    public void giveRandomReward(Player player) {
        var reward = (MilestoneReward)RewardUtils.getRandomReward(rewards,null);
        if (reward == null) return;
        reward.getReward().give(player);
    }
}
