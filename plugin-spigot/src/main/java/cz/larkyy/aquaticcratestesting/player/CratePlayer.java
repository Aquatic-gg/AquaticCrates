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
        return virtualKeys.getOrDefault(id,0);
    }

    public Map<String, Integer> getVirtualKeys() {
        return virtualKeys;
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

    public void takeKeys(String id, int amount) {
        if (!virtualKeys.containsKey(id)) {
            return;
        }
        int i = virtualKeys.get(id);
        i -= amount;
        if (i < 0) {
            i = 0;
        }
        virtualKeys.put(id,i);
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

    public boolean hasKey(Key key, int amount) {
        int keys = getKeys(key.getIdentifier());
        if (keys >= amount) {
            return true;
        }
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
        int keys = getKeys(key.getIdentifier());
        if (keys < 1) {
            return false;
        }
        virtualKeys.put(key.getIdentifier(),keys-1);
        return true;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isInAnimation() {
        return AquaticCratesAPI.getPlayerHandler().isInAnimation(player);
    }

    public static CratePlayer get(Player player) {
        return AquaticCratesAPI.getPlayer(player);
    }
}
