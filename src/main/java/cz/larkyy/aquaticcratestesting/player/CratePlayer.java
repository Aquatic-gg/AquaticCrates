package cz.larkyy.aquaticcratestesting.player;

import cz.larkyy.aquaticcratestesting.api.AquaticCratesAPI;
import cz.larkyy.aquaticcratestesting.crate.Key;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CratePlayer {

    private final Player player;
    private final Map<String, Integer> virtualKeys;

    public CratePlayer(Player player) {
        this.player = player;
        this.virtualKeys = new HashMap<>();
    }

    public int getKeys(String id) {
        if (!virtualKeys.containsKey(id)) {
            return 0;
        }
        return virtualKeys.get(id);
    }

    public void addKeys(String id, int amount) {
        int keys = getKeys(id);
        virtualKeys.put(id,keys+amount);
    }

    public boolean takeKey(String id) {
        Key key = Key.get(id);
        if (key == null) return false;
        return takeKey(key);
    }

    public boolean takeKey(Key key) {
        if (takePhysicalKey(key)) {
            return true;
        } else return takeVirtualKey(key);

    }

    private boolean takePhysicalKey(Key key) {
        for (ItemStack is : player.getInventory().getContents()) {
            if (is == null) continue;
            if (key.isItemKey(is)) {
                is.setAmount(is.getAmount()-1);
                return true;
            }
        }
        return false;
    }

    private boolean takeVirtualKey(Key key) {
        int keys = virtualKeys.get(key.getIdentifier());
        if (keys < 1) {
            return false;
        }
        virtualKeys.put(key.getIdentifier(),keys-1);
        return true;
    }

    public static CratePlayer get(Player player) {
        return AquaticCratesAPI.getPlayer(player);
    }
}
