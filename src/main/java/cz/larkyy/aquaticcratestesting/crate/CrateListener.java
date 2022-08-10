package cz.larkyy.aquaticcratestesting.crate;

import cz.larkyy.aquaticcratestesting.api.events.CrateInteractEvent;
import cz.larkyy.aquaticcratestesting.api.events.KeyInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CrateListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getHand() == null) return;
        if (e.getHand() == EquipmentSlot.OFF_HAND) return;

        Player p = e.getPlayer();
        ItemStack is = e.getItem();
        Location location = null;
        if (e.getClickedBlock() != null) {
            location = e.getClickedBlock().getLocation();
        }

        PlacedCrate placedCrate = PlacedCrate.get(location);
        if (placedCrate != null) {
            e.setCancelled(true);
            CrateInteractEvent event = new CrateInteractEvent(p,placedCrate);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            p.sendMessage("§fYou have interacted the crate!");
            return;
        }

        Key key = Key.get(is);
        if (key != null) {
            e.setCancelled(true);
            KeyInteractEvent event = new KeyInteractEvent(p,key,location);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            p.sendMessage("§fYou have interacted the key!");
            return;
        }
        Crate crate = Crate.get(is);
        if (crate != null) {
            if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
            e.setCancelled(true);
            Location loc = location.clone().add(0.5,1,0.5);
            loc.setYaw(p.getLocation().getYaw()+180);
            crate.spawn(loc);
        }
    }
}
