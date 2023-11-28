package cz.larkyy.aquaticcratestesting.crate.milestone;

import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import cz.larkyy.aquaticcratestesting.utils.IReward;

public class MilestoneReward implements IReward {

    private final Reward reward;
    private final double chance;

    public MilestoneReward(Reward reward, double chance) {
        this.chance = chance;
        this.reward = reward;
    }

    public Reward getReward() {
        return reward;
    }

    public double getChance() {
        return chance;
    }
}
