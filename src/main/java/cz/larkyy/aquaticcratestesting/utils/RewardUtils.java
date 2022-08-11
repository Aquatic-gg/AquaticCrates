package cz.larkyy.aquaticcratestesting.utils;

import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RewardUtils {

    public static Reward getRandomReward(Player player, List<Reward> rewards) {
        List<Reward> rs = getPossibleRewards(player,rewards);

        if (rs.isEmpty()) {
            return null;
        }

        if (getTotalPercentage(rs) > 0) {
            // Compute the total weight of all items together
            double totalWeight = 0.0d;
            for (Reward winning1 : rs) {
                totalWeight += winning1.getChance();
            }

            // Now choose a random item
            int randomIndex = -1;
            double random = Math.random() * totalWeight;
            for (int i = 0; i < rs.size(); ++i) {
                random -= rs.get(i).getChance();
                if (random <= 0.0d) {
                    randomIndex = i;
                    break;
                }
            }
            return rs.get(randomIndex);
        }
        return null;
    }

    public static List<Reward> getPossibleRewards(Player player, List<Reward> rewards) {
        List<Reward> rewardList = new ArrayList<>();
        for (Reward r : rewards) {
            if (r.getPermission() == null) {
                rewardList.add(r);
            } else {
                if (player.hasPermission(r.getPermission())) {
                    rewardList.add(r);
                }
            }
        }
        return rewardList;
    }

    private static double getTotalPercentage(List<Reward> rs) {
        return rs.stream().mapToDouble(Reward::getChance).sum();
    }

}
