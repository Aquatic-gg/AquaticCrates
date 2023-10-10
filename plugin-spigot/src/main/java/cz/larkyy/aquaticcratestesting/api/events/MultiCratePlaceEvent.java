package cz.larkyy.aquaticcratestesting.api.events;

import cz.larkyy.aquaticcratestesting.crate.PlacedMultiCrate;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MultiCratePlaceEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final PlacedMultiCrate crate;
    private Material blockMaterial;

    public MultiCratePlaceEvent(PlacedMultiCrate crate) {
        this.crate = crate;
        this.blockMaterial = crate.getMultiCrate().getBlockType();
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

    public PlacedMultiCrate getCrate() {
        return crate;
    }

    public void setBlockMaterial(Material blockMaterial) {
        this.blockMaterial = blockMaterial;
    }

}
