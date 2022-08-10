package cz.larkyy.aquaticcratestesting.api.events;

import cz.larkyy.aquaticcratestesting.crate.PlacedCrate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CrateInteractEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final PlacedCrate placedCrate;
    private boolean isCancelled;

    public CrateInteractEvent(Player player, PlacedCrate placedCrate) {
        this.player =player;
        this.placedCrate = placedCrate;
        isCancelled = false;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }

    public PlacedCrate getKey() {
        return placedCrate;
    }

    public Player getPlayer() {
        return player;
    }

}
