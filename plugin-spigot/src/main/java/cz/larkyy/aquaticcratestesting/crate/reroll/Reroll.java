package cz.larkyy.aquaticcratestesting.crate.reroll;

import cz.larkyy.aquaticcratestesting.api.AquaticCratesAPI;
import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public abstract class Reroll {
    private final Player player;
    private final Crate crate;
    private boolean rerolling;

    private final RerollManager rerollManager;

    private int reroll;
    private AtomicReference<Reward> reward;
    private final Consumer<Reward> claimConsumer;
    private final Consumer<Reward> rerollConsumer;

    public Reroll(RerollManager rerollManager, Player player, AtomicReference<Reward> reward, Consumer<Reward> claimConsumer, Consumer<Reward> rerollConsumer) {
        this.player = player;
        this.crate = rerollManager.getCrate();
        this.reroll = 0;
        this.reward = reward;
        this.rerollManager = rerollManager;
        rerolling = false;
        this.claimConsumer = claimConsumer;
        this.rerollConsumer = rerollConsumer;
    }

    public abstract void activate(Event e);

    public abstract void open();
    public void reroll() {
        reroll++;
        rerolling = false;
        reward.set(crate.getRandomReward(player));
        rerollConsumer.accept(reward.get());
    }

    public void claim() {
        rerolling = false;
        claimConsumer.accept(reward.get());
    }

    public int getReroll() {
        return reroll;
    }

    public boolean isRerolling() {
        return rerolling;
    }

    public void setRerolling(boolean rerolling) {
        this.rerolling = rerolling;
    }

    public Player getPlayer() {
        return player;
    }

    public RerollManager getRerollManager() {
        return rerollManager;
    }

    public Reward getReward() {
        return reward.get();
    }

    public static Reroll get(Player p) {
        return AquaticCratesAPI.getPlayerHandler().getRerollPlayer(p);
    }
}
