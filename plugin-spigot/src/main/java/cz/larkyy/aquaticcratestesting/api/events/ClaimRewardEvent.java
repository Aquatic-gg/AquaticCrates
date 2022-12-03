package cz.larkyy.aquaticcratestesting.api.events;

import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ClaimRewardEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Reward reward;
    private final Crate crate;

    public ClaimRewardEvent(Player player, Reward reward, Crate crate) {
        this.player =player;
        this.crate = crate;
        this.reward = reward;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public Crate getCrate() {
        return crate;
    }

    public Player getPlayer() {
        return player;
    }

    public Reward getReward() {
        return reward;
    }
}
