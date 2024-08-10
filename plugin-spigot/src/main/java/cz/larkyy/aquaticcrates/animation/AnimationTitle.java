package cz.larkyy.aquaticcrates.animation;

import cz.larkyy.aquaticcrates.AquaticCrates;
import gg.aquatic.aquaticseries.lib.StringExtKt;
import gg.aquatic.aquaticseries.lib.adapt.AquaticBossBar;
import gg.aquatic.aquaticseries.lib.adapt.AquaticString;

import java.util.ArrayList;
import java.util.List;

public class AnimationTitle {

    private final List<AquaticString> title;
    private final AquaticBossBar.Color color;
    private final AquaticBossBar.Style style;

    public AnimationTitle(List<String> title, AquaticBossBar.Color color, AquaticBossBar.Style style) {
        this.title = StringExtKt.toAquatic(title);
        this.color = color;
        this.style = style;
    }

    public List<AquaticString> getTitles() {
        return title;
    }

    public AquaticBossBar.Color getColor() {
        return color;
    }

    public AquaticBossBar.Style getStyle() {
        return style;
    }

    public List<AquaticBossBar> create() {
        List<AquaticBossBar> bars = new ArrayList<>();
        for (AquaticString str : title) {
            bars.add(AquaticCrates.aquaticSeriesLib.getAdapter().getBossBarAdapter().create(
                    str, color, style, 100.0
            ));
        }
        return bars;
    }
}
