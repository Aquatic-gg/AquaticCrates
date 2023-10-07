package cz.larkyy.aquaticcratestesting.nms;

import org.bukkit.event.Listener;

public abstract class Loader implements Listener {

    private final Runnable runnable;
    private boolean loaded = false;

    public Loader(Runnable runnable) {
        this.runnable = runnable;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isLoaded() {
        return loaded;
    }
}
