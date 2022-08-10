package cz.larkyy.aquaticcratestesting.crate;

import cz.larkyy.aquaticcratestesting.api.AquaticCratesAPI;
import cz.larkyy.aquaticcratestesting.item.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class Crate {

    private final String identifier;
    private final Key key;
    private final String model;

    public Crate(String identifier,CustomItem key, String model) {
        this.identifier = identifier;
        this.key = new Key(key,this);
        this.model = model;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Key getKey() {
        return key;
    }

    public void giveKey(Player player, int amount, boolean virtual) {
        if (virtual) AquaticCratesAPI.getPlayerHandler().getPlayer(player).addKeys(identifier,amount);
        else key.give(Collections.singletonList(player), amount);
    }

    public void giveKeyAll(int amount, boolean virtual) {
        if (virtual) {
            AquaticCratesAPI.getPlayerHandler().loadPlayers();
            AquaticCratesAPI.getPlayerHandler().getPlayers().forEach(
                    p -> p.addKeys(identifier,amount));
        }
        else key.give(new ArrayList<>(Bukkit.getOnlinePlayers()), amount);
    }

    public PlacedCrate spawn(Location location) {
        return new PlacedCrate(this,location,model);
    }

    public static Crate get(String identifier) {
        return AquaticCratesAPI.getCrate(identifier);
    }
}
