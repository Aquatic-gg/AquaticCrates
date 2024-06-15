package cz.larkyy.aquaticcrates.editor;

import cz.larkyy.aquaticcrates.crate.Crate;

import java.util.UUID;

public class EditingPlayer {

    private final UUID uuid;

    private final Crate originalCrate;
    private final Crate crate;

    public EditingPlayer(UUID uuid, Crate crate) {
        this.uuid = uuid;
        this.originalCrate = crate;
        this.crate = crate;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Crate getCrate() {
        return crate;
    }

    public Crate getOriginalCrate() {
        return originalCrate;
    }
}
