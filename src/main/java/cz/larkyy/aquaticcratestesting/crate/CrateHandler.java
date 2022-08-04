package cz.larkyy.aquaticcratestesting.crate;

import java.util.HashMap;
import java.util.Map;

public class CrateHandler {

    private final Map<String,Crate> crates;

    public CrateHandler() {
        crates = new HashMap<>();
    }

    public void addCrate(Crate crate) {
        crates.put(crate.getIdentifier(),crate);
    }

    public Crate getCrate(String identifier) {
        return getCrate(identifier);
    }

}
