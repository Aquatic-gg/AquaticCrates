package cz.larkyy.aquaticcrates.animation.task;

import cz.larkyy.aquaticcrates.AquaticCrates;
import gg.aquatic.aquaticseries.lib.StringExtKt;
import org.bukkit.entity.Player;

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
        AquaticCrates.aquaticSeriesLib.getAdapter().getTitleAdapter().send(
                player,
                StringExtKt.toAquatic(title),
                StringExtKt.toAquatic(subtitle),
                in,
                stay,
                out
        );
    }

}
