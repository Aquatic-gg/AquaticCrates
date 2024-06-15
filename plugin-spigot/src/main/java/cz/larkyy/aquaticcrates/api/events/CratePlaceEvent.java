package cz.larkyy.aquaticcrates.api.events;

import cz.larkyy.aquaticcrates.crate.PlacedCrate;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CratePlaceEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final PlacedCrate crate;
    private Material blockMaterial;

    public CratePlaceEvent(PlacedCrate crate) {
        this.crate = crate;
        this.blockMaterial = crate.getCrate().getBlockType();
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public Material getBlockMaterial() {
        return blockMaterial;
    }

    public PlacedCrate getCrate() {
        return crate;
    }

    public void setBlockMaterial(Material blockMaterial) {
        this.blockMaterial = blockMaterial;
    }
}
