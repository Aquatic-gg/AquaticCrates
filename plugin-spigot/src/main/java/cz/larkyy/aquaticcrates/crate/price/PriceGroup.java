package cz.larkyy.aquaticcrates.crate.price;

import cz.larkyy.aquaticcrates.crate.Crate;
import org.bukkit.entity.Player;

import java.util.List;

public class PriceGroup {

    private final List<ConfiguredPrice> prices;

    public PriceGroup(List<ConfiguredPrice> prices) {
        this.prices = prices;
    }

    public boolean has(Player player, Crate crate) {
        for (ConfiguredPrice price : prices) {
            if (!price.check(player,crate)) return false;
        }
        return true;
    }

    public void take(Player player, Crate crate) {
        for (ConfiguredPrice price : prices) {
            price.take(player, crate);
        }
    }
}
