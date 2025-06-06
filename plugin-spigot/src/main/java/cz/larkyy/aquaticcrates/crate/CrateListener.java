package cz.larkyy.aquaticcrates.crate;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.animation.RewardItem;
import cz.larkyy.aquaticcrates.api.AquaticCratesAPI;
import cz.larkyy.aquaticcrates.api.events.CrateInteractEvent;
import cz.larkyy.aquaticcrates.api.events.KeyInteractEvent;
import cz.larkyy.aquaticcrates.api.events.MultiCrateInteractEvent;
import cz.larkyy.aquaticcrates.crate.inventories.PreviewGUI;
import cz.larkyy.aquaticcrates.crate.inventories.RerollGUI;
import cz.larkyy.aquaticcrates.crate.reroll.Reroll;
import cz.larkyy.aquaticcrates.player.CratePlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class CrateListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        var topInv = e.getView().getTopInventory();
        if (topInv.getHolder() instanceof PreviewGUI) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        PlacedCrate pc = PlacedCrate.get(e.getBlock().getLocation());
        if (pc != null) {
            Player p = e.getPlayer();
            e.setCancelled(true);
            Bukkit.getPluginManager().callEvent(new CrateInteractEvent(p,pc,Action.LEFT_CLICK_BLOCK,e.getBlock().getLocation()));
            return;
        }
        PlacedMultiCrate pmc = AquaticCrates.getCrateHandler().getPlacedMultiCrate(e.getBlock().getLocation());
        if (pmc != null) {
            Player p = e.getPlayer();
            e.setCancelled(true);
            Bukkit.getPluginManager().callEvent(new MultiCrateInteractEvent(p,pmc,Action.LEFT_CLICK_BLOCK,e.getBlock().getLocation()));
            return;
        }
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

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        var is = e.getItemInHand();
        Key key = Key.get(is);
        if (key != null) {
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
        PlacedMultiCrate placedMultiCrate = AquaticCrates.getCrateHandler().getPlacedMultiCrate(location);
        if (placedMultiCrate != null) {
            e.setCancelled(true);
            Bukkit.getPluginManager().callEvent(new MultiCrateInteractEvent(p,placedMultiCrate,action,e.getClickedBlock().getLocation()));
            return;
        }

        Key key = Key.get(is);
        if (key != null) {
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
            return;
        }
        MultiCrate multiCrate = MultiCrate.get(is);
        if (multiCrate != null) {
            if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
            e.setCancelled(true);
            Location loc = location.clone().add(0.5,1,0.5);
            loc.setYaw(p.getLocation().getYaw()+180);
            AquaticCrates.getCrateHandler().spawnMultiCrate(loc,multiCrate);
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
            if (AquaticCrates.getCrateHandler().skipAnimation(e.getPlayer())) {
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
    public void onMultiCrateInteract(MultiCrateInteractEvent e) {
        Player p = e.getPlayer();
        PlacedMultiCrate pc = e.getPlacedCrate();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Key key = Key.get(p.getInventory().getItemInMainHand());
            if (key != null) {
                if (pc.getMultiCrate().getCrates().contains(key.getCrate().getIdentifier())) {
                    key.getCrate().open(CratePlayer.get(p),pc.getPlacedCrates().get(key.getCrate().getIdentifier()), p.isSneaking());
                    return;
                }
            }
            pc.getMultiCrate().openPreview(p,pc);
        } else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (p.isSneaking() && p.hasPermission("aquaticcrates.break")) {
                AquaticCratesAPI.getCrateHandler().removePlacedMultiCrate(e.getLocation());
            } else {
                pc.getMultiCrate().openPreview(p,pc);
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
