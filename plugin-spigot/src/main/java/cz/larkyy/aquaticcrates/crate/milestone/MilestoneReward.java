package cz.larkyy.aquaticcrates.crate.milestone;

import cz.larkyy.aquaticcrates.crate.reward.Reward;
import gg.aquatic.aquaticseries.lib.chance.IChance;

public class MilestoneReward implements IChance {

    private final Reward reward;
    private final double chance;

    public MilestoneReward(Reward reward, double chance) {
        this.chance = chance;
        this.reward = reward;
    }

    public Reward getReward() {
        return reward;
    }

    @Override
    public double getChance() {
        return chance;
    }
}
