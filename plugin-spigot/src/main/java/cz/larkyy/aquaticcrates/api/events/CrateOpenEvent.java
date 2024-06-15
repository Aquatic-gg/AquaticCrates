package cz.larkyy.aquaticcrates.api.events;

import cz.larkyy.aquaticcrates.crate.Crate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CrateOpenEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Crate crate;
    private boolean isCancelled;

    public CrateOpenEvent(Player player, Crate crate) {
        this.player =player;
        this.crate = crate;
        isCancelled = false;
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
}
