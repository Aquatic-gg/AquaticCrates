package cz.larkyy.aquaticcratestesting.api.events;

import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.crate.Key;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class KeyUseEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Crate crate;
    private boolean isCancelled;
    private final KeyType keyType;
    private final ItemStack itemStack;

    public KeyUseEvent(Player player, Crate crate, KeyType keyType, ItemStack itemStack) {
        this.player =player;
        this.crate = crate;
        isCancelled = false;
        this.keyType = keyType;
        this.itemStack = itemStack;
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

    public KeyType getKeyType() {
        return keyType;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Crate getCrate() {
        return crate;
    }

    public Player getPlayer() {
        return player;
    }

    public enum KeyType {
        NONE,
        VIRTUAL,
        PHYSICAL
    }
}