package cz.larkyy.aquaticcrates.crate.milestone;

import cz.larkyy.aquaticcrates.utils.IReward;
import cz.larkyy.aquaticcrates.utils.RewardUtils;
import org.bukkit.entity.Player;

import java.util.List;

public class Milestone {

    private final int milestone;
    private final String displayName;
    private final List<IReward> rewards;

    public Milestone(int milestone, List<IReward> rewards, String displayName) {
        this.milestone = milestone;
        this.rewards = rewards;
        this.displayName = displayName;
    }

    public String getDisplayName() {
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
