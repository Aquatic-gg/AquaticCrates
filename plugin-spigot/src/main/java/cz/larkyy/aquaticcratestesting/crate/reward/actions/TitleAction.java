package cz.larkyy.aquaticcratestesting.crate.reward.actions;

import cz.larkyy.aquaticcratestesting.crate.reward.RewardAction;
import org.bukkit.entity.Player;

public class TitleAction extends RewardAction {

    private final String title;
    private final String subtitle;
    private final int in;
    private final int stay;
    private final int out;

    public TitleAction(String title, String subtitle, int in, int stay, int out) {
        this.title = title;
        this.subtitle = subtitle;
        this.in = in;
        this.out = out;
        this.stay = stay;
    }

    @Override
    public void run(Player player) {
        player.sendTitle(title,subtitle,in,stay,out);
    }
}
