package cz.larkyy.aquaticcrates.crate.price;

import org.bukkit.entity.Player;

import java.util.List;

public class PriceGroup {

    private final List<OpenPrice> prices;

    public PriceGroup(List<OpenPrice> prices) {
        this.prices = prices;
    }

    public boolean has(Player player) {
        for (OpenPrice price : prices) {
            if (!price.has(player)) {
                return false;
            }
        }
        return true;
    }

    public void take(Player player) {
        for (OpenPrice price : prices) {
            price.take(player);
        }
    }
}
