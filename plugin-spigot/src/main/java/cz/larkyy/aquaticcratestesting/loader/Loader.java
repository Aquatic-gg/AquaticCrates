package cz.larkyy.aquaticcratestesting.loader;

public abstract class Loader {

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
