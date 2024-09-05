package cz.larkyy.aquaticcrates.animation;

import cz.larkyy.aquaticcrates.animation.impl.CinematicAnimation;
import cz.larkyy.aquaticcrates.animation.impl.PlacedCratePersonalisedAnimation;
import cz.larkyy.aquaticcrates.animation.task.PreOpenTitle;
import cz.larkyy.aquaticcrates.crate.Crate;
import cz.larkyy.aquaticcrates.crate.PlacedCrate;
import cz.larkyy.aquaticcrates.animation.impl.InstantAnimation;
import cz.larkyy.aquaticcrates.animation.impl.PlacedCrateAnimation;
import cz.larkyy.aquaticcrates.crate.reroll.Reroll;
import cz.larkyy.aquaticcrates.crate.reward.Reward;
import cz.larkyy.aquaticcrates.messages.Messages;
import gg.aquatic.aquaticseries.lib.action.ConfiguredAction;
import gg.aquatic.aquaticseries.lib.adapt.AquaticBossBar;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholder;
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholders;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class AnimationManager {

    private final Crate crate;
    private final Type type;

    private final TreeMap<Integer, List<ConfiguredAction<Animation>>> tasks;
    private final int length;
    private final int startDelay;
    private final PreOpenTitle preOpenTitle;
    private final AnimationTitle openingTitle;
    private final AnimationTitle rerollingTitle;
    private final Location modelLocation;
    private final Location cameraLocation;
    private final Map<Player,Animation> animations;
    private final boolean skippable;
    private final boolean setPumpkinHelmet;
    private final Map<Player, List<AquaticBossBar>> bossBars;


    public enum Type {
        INSTANT,
        PLACEDCRATE,
        PLACEDCRATE_PERSONALISED,
        CINEMATIC
    }

    public AnimationManager(Crate crate, Type type, TreeMap<Integer, List<ConfiguredAction<Animation>>> tasks, int length, AnimationTitle openingTitle, AnimationTitle rerollingTitle,
                            Location modelLocation, Location cameraLocation, boolean skippable, boolean setPumpkinHelmet,
                            int startDelay, PreOpenTitle preOpenTitle) {
        this.crate = crate;
        this.type = type;
        this.tasks = tasks;
        this.length = length;
        this.animations = new HashMap<>();
        this.openingTitle = openingTitle;
        this.rerollingTitle = rerollingTitle;
        this.modelLocation = modelLocation;
        this.cameraLocation = cameraLocation;
        this.skippable = skippable;
        this.setPumpkinHelmet = setPumpkinHelmet;
        this.preOpenTitle = preOpenTitle;
        this.startDelay = startDelay;

        bossBars = new HashMap<>();
    }

    public void open(Player p, AtomicReference<Reward> reward, PlacedCrate pc, Consumer<Animation> callback) {
        switch (type) {
            case INSTANT -> {
                new InstantAnimation(this,p,reward,callback);
            }
            case PLACEDCRATE -> {
                new PlacedCrateAnimation(this,p,reward,callback,pc);
            }
            case CINEMATIC -> {
                new CinematicAnimation(this,p,reward,callback);
            }
            case PLACEDCRATE_PERSONALISED -> {
                new PlacedCratePersonalisedAnimation(this,p,reward,callback,pc);
            }
        }
    }

    public void playTask(int i, Animation animation) {
        var list = tasks.get(i);
        if (list == null) return;
        for (ConfiguredAction<Animation> animationConfiguredAction : list) {
            animationConfiguredAction.run(animation, (p, s) -> s.replace("%player%", animation.getPlayer().getName()));
        }
    }

    public boolean shouldStopAnimation(int i) {
        return (i >= length);
    }

    public boolean setPumpkinHelmet() {
        return setPumpkinHelmet;
    }

    public void removeAnimation(Player player) {
        animations.remove(player);
    }
    public void addAnimation(Player player,Animation animation) {
        animations.put(player, animation);
    }

    public Crate getCrate() {
        return crate;
    }

    public boolean canBeOpened(Player player) {
        if (type == Type.PLACEDCRATE) {
            if (!animations.isEmpty()) {
                Messages.OPEN_ALREADY_OPENING.send(player);
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public boolean isAnyoneOpening() {
        return !animations.isEmpty();
    }


    public void showTitle(AnimationTitle title, Player p) {
        List<AquaticBossBar> list = title.create();
        for (var item : list) {
            var rp = Reroll.get(p);
            if (rp == null) {
                continue;
            }
            item.setText(item.getText().replace("%rerolls-available%",
                    (crate.getRerollManager().getPlayerLimit(p) - rp.getReroll())+""));
        }

        if (bossBars.containsKey(p)) {
            hideTitle(p);
        }

        list.forEach(bb -> {
            bb.addPlayer(p);
        });
        bossBars.put(p,list);

    }

    public void hideTitle(Player p) {
        if (!bossBars.containsKey(p)) {
            return;
        }
        bossBars.get(p).forEach(bb -> {
            bb.removePlayer(p);
        });
        bossBars.remove(p);
    }

    public AnimationTitle getOpeningTitle() {
        return openingTitle;
    }

    public AnimationTitle getRerollingTitle() {
        return rerollingTitle;
    }

    public Location getCameraLocation() {
        return cameraLocation;
    }

    public Location getModelLocation() {
        return modelLocation;
    }

    public Animation getAnimation(Player p) {
        return animations.get(p);
    }

    public boolean isSkippable() {
        return skippable;
    }

    public boolean isInAnimation(Player p) {
        return animations.containsKey(p);
    }

    public int getStartDelay() {
        return startDelay;
    }

    public PreOpenTitle getPreOpenTitle() {
        return preOpenTitle;
    }

    public boolean skipAnimation(Player p) {
        Animation animation = getAnimation(p);
        if (animation == null) {
            return false;
        }
        if (!isSkippable()) {
            return false;
        }
        if (!animation.isStarted()) {
            return false;
        }
        animation.reroll();
        return true;
    }

    public boolean forceSkipAnimation(Player p) {
        Animation animation = getAnimation(p);
        if (animation == null) {
            return false;
        }
        animation.reroll();
        return true;
    }
}
