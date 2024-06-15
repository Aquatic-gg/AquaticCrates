package cz.larkyy.aquaticcrates.crate.price;

import cz.larkyy.aquaticcrates.crate.Crate;
import org.bukkit.entity.Player;

import java.util.Map;

public class ConfiguredPrice {

    private final OpenPrice priceType;
    private final Map<String,Object> values;

    public ConfiguredPrice(OpenPrice priceType, Map<String,Object> values) {
        this.priceType = priceType;
        this.values = values;
    }

    public boolean check(Player player, Crate crate) {
        return priceType.check(player,crate,values);
    }

    public void take(Player player, Crate crate) {
        priceType.take(player,crate,values);
    }

}
