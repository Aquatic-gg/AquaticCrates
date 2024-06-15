package cz.larkyy.aquaticcrates.crate.milestone;

import cz.larkyy.aquaticcrates.crate.reward.Reward;
import cz.larkyy.aquaticcrates.utils.IReward;

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
