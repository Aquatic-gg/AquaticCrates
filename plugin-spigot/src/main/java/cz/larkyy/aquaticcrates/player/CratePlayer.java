package cz.larkyy.aquaticcrates.player;

import cz.larkyy.aquaticcrates.api.AquaticCratesAPI;
import cz.larkyy.aquaticcrates.api.events.KeyUseEvent;
import cz.larkyy.aquaticcrates.crate.Key;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CratePlayer {

    private final UUID uuid;
    private final Map<String, Integer> virtualKeys;

    public CratePlayer(UUID uuid) {
        this.uuid = uuid;
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
        for (ItemStack is : getPlayer().getInventory().getContents()) {
            if (is == null) continue;
            if (key.isItemKey(is)) {
                is.setAmount(is.getAmount()-1);
                var event = new KeyUseEvent(getPlayer(),key.getCrate(), KeyUseEvent.KeyType.PHYSICAL,is);
                Bukkit.getPluginManager().callEvent(event);
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
        for (ItemStack is : getPlayer().getInventory().getContents()) {
            if (is == null) continue;
            if (key.isItemKey(is)) {
                if (is.getAmount() >= amount) return true;
            }
        }
        return false;
    }

    public int getKeysAmount(Key key) {
        int amt = getKeys(key.getIdentifier());
        for (ItemStack is : getPlayer().getInventory().getContents()) {
            if (is == null) continue;
            if (key.isItemKey(is)) {
                amt += is.getAmount();
            }
        }
        return amt;
    }

    private boolean takeVirtualKey(Key key) {
        int keys = getKeys(key.getIdentifier());
        if (keys < 1) {
            return false;
        }
        virtualKeys.put(key.getIdentifier(),keys-1);

        var event = new KeyUseEvent(getPlayer(),key.getCrate(), KeyUseEvent.KeyType.VIRTUAL,null);
        Bukkit.getPluginManager().callEvent(event);
        return true;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public boolean isInAnimation() {
        return AquaticCratesAPI.getPlayerHandler().isInAnimation(getPlayer());
    }

    public static CratePlayer get(Player player) {
        return AquaticCratesAPI.getPlayer(player);
    }
}
