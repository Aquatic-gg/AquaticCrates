package cz.larkyy.aquaticcrates.crate.price;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.crate.price.types.KeyPrice;

import java.util.HashMap;
import java.util.Map;

public class OpenPrices {

    private final Map<String, OpenPrice> priceTypes = new HashMap<>() {
        {
            put("key",new KeyPrice());
        }
    };

    public void registerPriceType(String id, OpenPrice task) {
        priceTypes.put(id,task);
    }

    public void unregisterPriceType(String id) {
        priceTypes.remove(id);
    }

    public OpenPrice getPriceType(String string) {
        return priceTypes.get(string.toLowerCase());
    }

    public static OpenPrices inst() {
        return AquaticCrates.getOpenPrices();
    }

}
