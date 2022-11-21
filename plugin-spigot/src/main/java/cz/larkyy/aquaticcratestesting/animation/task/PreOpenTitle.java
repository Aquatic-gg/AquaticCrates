package cz.larkyy.aquaticcratestesting.animation.task;

import org.bukkit.entity.Player;
import xyz.larkyy.colorutils.Colors;

public class PreOpenTitle {

    private final int in;
    private final int out;
    private final int stay;
    private final String title;
    private final String subtitle;

    public PreOpenTitle(int in, int out, int stay, String title, String subtitle) {
        this.in = in;
        this.out = out;
        this.stay = stay;
        this.title = title;
        this.subtitle = subtitle;
    }

    public void show(Player player) {
        player.sendTitle(
                Colors.format(title),
                Colors.format(subtitle),
                in,
                stay,
                out
        );
    }

}
