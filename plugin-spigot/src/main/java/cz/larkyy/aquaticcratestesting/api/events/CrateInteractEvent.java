package cz.larkyy.aquaticcratestesting.api.events;

import cz.larkyy.aquaticcratestesting.crate.PlacedCrate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.jetbrains.annotations.NotNull;

public class CrateInteractEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final PlacedCrate placedCrate;
    private final Action action;
    private final Location location;
    private boolean isCancelled;

    public CrateInteractEvent(Player player, PlacedCrate placedCrate, Action action, Location location) {
        this.player =player;
        this.placedCrate = placedCrate;
        this.action = action;
        this.location = location;
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

    public PlacedCrate getPlacedCrate() {
        return placedCrate;
    }

    public Player getPlayer() {
        return player;
    }

    public Action getAction() {
        return action;
    }

    public Location getLocation() {
        return location;
    }
}
