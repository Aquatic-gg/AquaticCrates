package cz.larkyy.aquaticcrates.player;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.crate.reroll.Reroll;
import gg.aquatic.aquaticseries.lib.audience.WhitelistAudience;
import gg.aquatic.aquaticseries.lib.interactable2.AbstractSpawnedPacketInteractable;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        AquaticCrates.getPlayerHandler().loadPlayer(p, player -> {});
        AquaticCrates.getCrateHandler().getLocations().values().forEach(v -> {
            //v.getHologram().spawn(new WhitelistAudience(new ArrayList<>(){{add(p.getUniqueId());}}), list -> {});
            var interactable = v.getSpawnedInteractable();
            if (interactable != null) {
                if (interactable instanceof AbstractSpawnedPacketInteractable<?> packetInteractable) {
                    packetInteractable.show(p);
                }
            }
        });

        if (p.getPersistentDataContainer().has(Animation.KEY, PersistentDataType.INTEGER)) {
            p.setInvisible(false);
            p.setGameMode(GameMode.SURVIVAL);

            p.getPersistentDataContainer().remove(Animation.KEY);
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        /*
        AquaticCrates.getCrateHandler().getLocations().values().forEach(v -> {
            if (v.getLocation().getWorld().equals(e.getPlayer().getWorld())) {
                v.getHologram().spawn(new WhitelistAudience(new ArrayList<>() {{ add(p.getUniqueId()); }}), list -> {
                });
            }
        });
         */
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        AquaticCrates.getPlayerHandler().savePlayer(CratePlayer.get(e.getPlayer()));
        AquaticCrates.getPlayerHandler().unloadPlayer(e.getPlayer());

        AquaticCrates.getCrateHandler().forceSkipAnimation(e.getPlayer());
        Reroll rp = Reroll.get(e.getPlayer());
        if (rp != null) {
            if (rp.isRerolling()) {
                rp.claim();
                return;
            }
        }
    }

}
