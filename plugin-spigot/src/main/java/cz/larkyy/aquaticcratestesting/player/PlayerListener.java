package cz.larkyy.aquaticcratestesting.player;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.animation.Animation;
import cz.larkyy.aquaticcratestesting.crate.reroll.Reroll;
import org.bukkit.Bukkit;
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
        AquaticCratesTesting.getPlayerHandler().loadPlayer(p,player -> {});
        AquaticCratesTesting.getCrateHandler().getLocations().values().forEach(v -> {
            v.getHologram().spawn(Arrays.asList(p), list -> {});
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
        AquaticCratesTesting.getCrateHandler().getLocations().values().forEach(v -> {
            if (v.getLocation().getWorld().equals(e.getPlayer().getWorld())) {
                v.getHologram().spawn(Arrays.asList(p), list -> {
                });
            }
        });
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        AquaticCratesTesting.getPlayerHandler().savePlayer(CratePlayer.get(e.getPlayer()));
        AquaticCratesTesting.getPlayerHandler().unloadPlayer(e.getPlayer());

        AquaticCratesTesting.getCrateHandler().forceSkipAnimation(e.getPlayer());
        Reroll rp = Reroll.get(e.getPlayer());
        if (rp != null) {
            if (rp.isRerolling()) {
                rp.claim();
                return;
            }
        }
    }

}
