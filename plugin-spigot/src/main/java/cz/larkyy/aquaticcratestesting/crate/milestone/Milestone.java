package cz.larkyy.aquaticcratestesting.crate.milestone;

import cz.larkyy.aquaticcratestesting.utils.IReward;
import cz.larkyy.aquaticcratestesting.utils.RewardUtils;
import org.bukkit.entity.Player;

import java.util.List;

public class Milestone {

    private final int milestone;
    private final List<IReward> rewards;

    public Milestone(int milestone, List<IReward> rewards) {
        this.milestone = milestone;
        this.rewards = rewards;
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
