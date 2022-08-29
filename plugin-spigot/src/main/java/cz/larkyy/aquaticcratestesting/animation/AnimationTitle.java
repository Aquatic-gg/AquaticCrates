package cz.larkyy.aquaticcratestesting.animation;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import xyz.larkyy.colorutils.Colors;

import java.util.ArrayList;
import java.util.List;

public class AnimationTitle {

    private final List<String> title;
    private final BarColor color;
    private final BarStyle style;

    public AnimationTitle(List<String> title, BarColor color, BarStyle style) {
        this.title = Colors.format(title);
        this.color = color;
        this.style = style;
    }

    public List<String> getTitles() {
        return title;
    }

    public BarColor getColor() {
        return color;
    }

    public BarStyle getStyle() {
        return style;
    }

    public List<BossBar> create() {
        List<BossBar> bars = new ArrayList<>();
        for (String str : title) {
            bars.add(Bukkit.createBossBar(str,color,style));
        }
        return bars;
    }
}
