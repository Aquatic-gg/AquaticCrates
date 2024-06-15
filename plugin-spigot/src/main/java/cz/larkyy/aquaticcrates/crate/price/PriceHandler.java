package cz.larkyy.aquaticcrates.crate.price;

import cz.larkyy.aquaticcrates.crate.Crate;
import org.bukkit.entity.Player;

import java.util.List;

public class PriceHandler {

    private final List<PriceGroup> priceGroups;

    public PriceHandler(List<PriceGroup> priceGroups) {
        this.priceGroups = priceGroups;
    }

    public PriceGroup chooseGroup(Player player, Crate crate) {
        for (PriceGroup priceGroup : priceGroups) {
            if (priceGroup.has(player,crate)) return priceGroup;
        }
        return null;
    }

    public List<PriceGroup> getPriceGroups() {
        return priceGroups;
    }
}
