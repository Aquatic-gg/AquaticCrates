package cz.larkyy.aquaticcratestesting.animation;

import cz.larkyy.aquaticcratestesting.animation.impl.CinematicAnimation;
import cz.larkyy.aquaticcratestesting.animation.impl.PlacedCratePersonalisedAnimation;
import cz.larkyy.aquaticcratestesting.animation.task.ConfiguredTask;
import cz.larkyy.aquaticcratestesting.animation.task.PreOpenTitle;
import cz.larkyy.aquaticcratestesting.animation.task.Task;
import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.crate.PlacedCrate;
import cz.larkyy.aquaticcratestesting.animation.impl.InstantAnimation;
import cz.larkyy.aquaticcratestesting.animation.impl.PlacedCrateAnimation;
import cz.larkyy.aquaticcratestesting.crate.reroll.Reroll;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import cz.larkyy.aquaticcratestesting.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import xyz.larkyy.itemlibrary.impl.OraxenItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class AnimationManager {

    private final Crate crate;
    private final Type type;

    private final List<ConfiguredTask> tasks;
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
    private final Map<Player, List<BossBar>> bossBars;
    private final AnimationEmote emote;


    public enum Type {
        INSTANT,
        PLACEDCRATE,
        PLACEDCRATE_PERSONALISED,
        CINEMATIC
    }

    public AnimationManager(Crate crate, Type type, List<ConfiguredTask> tasks, int length, AnimationTitle openingTitle, AnimationTitle rerollingTitle,
                            Location modelLocation, Location cameraLocation, boolean skippable, boolean setPumpkinHelmet, AnimationEmote emote,
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
        this.emote = emote;
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
                new CinematicAnimation(this,p,reward,emote,callback);
            }
            case PLACEDCRATE_PERSONALISED -> {
                new PlacedCratePersonalisedAnimation(this,p,reward,callback,pc);
            }
        }
    }

    public void playTask(int i, Animation animation) {
        tasks.forEach(task -> {
            if (i == task.getDelay()) {
                task.run(animation);
            }
        });
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


    public List<BossBar> showTitle(AnimationTitle title, Player p) {
        List<BossBar> list = title.create();
        for (var item : list) {
            var rp = Reroll.get(p);
            if (rp == null) {
                continue;
            }
            item.setTitle(item.getTitle().replace("%rerolls-available%",
                    (crate.getRerollManager().getPlayerLimit(p) - rp.getReroll())+""));
        }

        if (bossBars.containsKey(p)) {
            hideTitle(p);
        }

        list.forEach(bb -> {
            bb.addPlayer(p);
        });
        bossBars.put(p,list);

        return list;
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
