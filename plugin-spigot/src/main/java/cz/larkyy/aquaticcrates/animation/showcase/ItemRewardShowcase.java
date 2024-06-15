package cz.larkyy.aquaticcrates.animation.showcase;

import org.bukkit.Location;
import org.bukkit.entity.Item;

public class ItemRewardShowcase implements RewardShowcase{

    private final Item item;

    public ItemRewardShowcase(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public Location getLocation() {
        return item.getLocation();
    }

    @Override
    public void destroy() {
        item.remove();
    }
}
