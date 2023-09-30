package cz.larkyy.aquaticcratestesting.crate;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.animation.RewardItem;
import cz.larkyy.aquaticcratestesting.api.AquaticCratesAPI;
import cz.larkyy.aquaticcratestesting.api.events.CrateInteractEvent;
import cz.larkyy.aquaticcratestesting.api.events.KeyInteractEvent;
import cz.larkyy.aquaticcratestesting.crate.reroll.Reroll;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class CrateListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        PlacedCrate pc = PlacedCrate.get(e.getBlock().getLocation());
        if (pc == null) {
            return;
        }
        Player p = e.getPlayer();
        e.setCancelled(true);
        Bukkit.getPluginManager().callEvent(new CrateInteractEvent(p,pc,Action.LEFT_CLICK_BLOCK,e.getBlock().getLocation()));
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player p)) {
            return;
        }

        Reroll rp = Reroll.get(p);
        if (rp != null) {
            if (rp.isRerolling()) {
                rp.activate(e);
            }
        }
    }

    @EventHandler
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player p)) {
            return;
        }

        Reroll rp = Reroll.get(p);
        if (rp != null) {
            if (rp.isRerolling()) {
                rp.activate(new PlayerInteractEvent(p,Action.LEFT_CLICK_AIR,p.getInventory().getItemInMainHand(),null, BlockFace.SELF));
            }
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        if (e.getItem().getPersistentDataContainer().has(RewardItem.REWARD_ITEM_KEY, PersistentDataType.INTEGER)) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getHand() == null) return;
        if (e.getHand() == EquipmentSlot.OFF_HAND) return;

        Player p = e.getPlayer();
        Reroll rp = Reroll.get(p);
        if (rp != null) {
            if (rp.isRerolling()) {
                rp.activate(e);
                return;
            }
        }

        ItemStack is = e.getItem();
        Location location = null;

        Action action = e.getAction();
        if (p.getGameMode() == GameMode.ADVENTURE) {
            if (e.getAction() == Action.LEFT_CLICK_AIR) {
                var b = p.getTargetBlockExact(5);
                if (b != null) {
                    location = b.getLocation();
                    action = Action.LEFT_CLICK_BLOCK;
                }
            }
        }

        if (e.getClickedBlock() != null) {
            location = e.getClickedBlock().getLocation();
        }

        PlacedCrate placedCrate = PlacedCrate.get(location);
        if (placedCrate != null) {
            e.setCancelled(true);
            Bukkit.getPluginManager().callEvent(new CrateInteractEvent(p,placedCrate,action,e.getClickedBlock().getLocation()));
            return;
        }

        Key key = Key.get(is);
        if (key != null) {
            e.setCancelled(true);
            Bukkit.getPluginManager().callEvent(new KeyInteractEvent(p,key,location,action));
            return;
        }
        Crate crate = Crate.get(is);
        if (crate != null) {
            if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
            e.setCancelled(true);
            Location loc = location.clone().add(0.5,1,0.5);
            loc.setYaw(p.getLocation().getYaw()+180);
            crate.spawn(loc);
            AquaticCratesAPI.getCrateHandler().saveCrates();
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {

        if (!e.isSneaking()) {
            return;
        }
        Player p = e.getPlayer();

        Reroll rp = Reroll.get(p);
        if (rp == null || !rp.isRerolling()) {
            if (AquaticCratesTesting.getCrateHandler().skipAnimation(e.getPlayer())) {
                return;
            }
        }
        else {
            rp.activate(e);
            return;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCrateInteract(CrateInteractEvent e) {
        Player p = e.getPlayer();
        PlacedCrate pc = e.getPlacedCrate();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            pc.open(CratePlayer.get(e.getPlayer()),p.isSneaking());
        } else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (p.isSneaking() && p.hasPermission("aquaticcrates.break")) {
                AquaticCratesAPI.getCrateHandler().removePlacedCrate(e.getLocation());
            } else {
                pc.getCrate().openPreview(p,pc);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onKeyInteract(KeyInteractEvent e) {
        Key key = e.getKey();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (key.requiresCrateToOpen()) {
                return;
            }
            key.getCrate().open(CratePlayer.get(e.getPlayer()),null,e.getPlayer().isSneaking());
        } else if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            key.getCrate().openPreview(e.getPlayer(),null);
        }
    }
}
