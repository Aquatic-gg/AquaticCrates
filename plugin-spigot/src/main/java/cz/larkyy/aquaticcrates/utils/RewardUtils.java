package cz.larkyy.aquaticcrates.utils;

import cz.larkyy.aquaticcrates.crate.Crate;
import cz.larkyy.aquaticcrates.crate.reward.Reward;
import gg.aquatic.aquaticseries.lib.chance.IChance;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RewardUtils {

    public static IChance getRandomReward(List<IChance> rs, IChance excludedReward) {
        if (excludedReward != null) {
            rs.remove(excludedReward);
            if (rs.isEmpty()) {
                return excludedReward;
            }
        }

        if (rs.isEmpty()) {
            return null;
        }

        if (getTotalPercentage(rs) > 0) {
            // Compute the total weight of all items together
            double totalWeight = 0.0d;
            for (var winning1 : rs) {
                totalWeight += winning1.chance();
            }

            // Now choose a random item
            int randomIndex = -1;
            double random = Math.random() * totalWeight;
            for (int i = 0; i < rs.size(); ++i) {
                random -= rs.get(i).chance();
                if (random <= 0.0d) {
                    randomIndex = i;
                    break;
                }
            }
            return rs.get(randomIndex);
        }
        return null;
    }

    public static List<IChance> getPossibleRewards(Player player, List<Reward> rewards) {
        List<IChance> rewardList = new ArrayList<>();
        for (Reward r : rewards) {
            if (r.getWinConditions().isEmpty()) {
                rewardList.add(r);
            } else if (r.getWinConditions().stream().allMatch(c -> c.check(player))) {
                rewardList.add(r);
            }
        }
        return rewardList;
    }

    private static double getTotalPercentage(List<IChance> rs) {
        return rs.stream().mapToDouble(IChance::chance).sum();
    }

}
