package cz.larkyy.aquaticcratestesting.crate;

import cz.larkyy.aquaticcratestesting.api.AquaticCratesAPI;
import cz.larkyy.aquaticcratestesting.item.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Crate {

    private final String identifier;
    private final Key key;

    public Crate(String identifier,CustomItem key) {
        this.identifier = identifier;
        this.key = new Key(key,this);
    }

    public String getIdentifier() {
        return identifier;
    }

    public Key getKey() {
        return key;
    }

    public void giveKey(Player player, int amount, boolean virtual) {

    }

    public void giveKeyAll(int amount, boolean virtual) {
        Bukkit.getOnlinePlayers().forEach(p->{

        });
    }

    public static Crate get(String identifier) {
        return AquaticCratesAPI.getCrate(identifier);
    }
}
