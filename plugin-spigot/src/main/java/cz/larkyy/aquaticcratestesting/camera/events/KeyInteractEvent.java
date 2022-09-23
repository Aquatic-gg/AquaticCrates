package cz.larkyy.aquaticcratestesting.camera.events;

import cz.larkyy.aquaticcratestesting.crate.Key;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.jetbrains.annotations.NotNull;

public class KeyInteractEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Action action;
    private final Key key;
    private final Location location;
    private boolean isCancelled;

    public KeyInteractEvent(Player player, Key key, Location location, Action action) {
        this.player =player;
        this.key = key;
        this.location = location;
        this.action = action;
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

    public Key getKey() {
        return key;
    }

    public Location getLocation() {
        return location;
    }

    public Player getPlayer() {
        return player;
    }

    public Action getAction() {
        return action;
    }
}
