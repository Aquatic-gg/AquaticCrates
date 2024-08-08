package cz.larkyy.aquaticcrates.crate.milestone;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.utils.IReward;
import cz.larkyy.aquaticcrates.utils.RewardUtils;
import gg.aquatic.aquaticseries.lib.adapt.AquaticString;
import org.bukkit.entity.Player;

import java.util.List;

public class Milestone {

    private final int milestone;
    private final AquaticString displayName;
    private final List<IReward> rewards;

    public Milestone(int milestone, List<IReward> rewards, AquaticString displayName) {
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
