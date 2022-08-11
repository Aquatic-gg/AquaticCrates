package cz.larkyy.aquaticcratestesting.player;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        AquaticCratesTesting.getPlayerHandler().loadPlayer(e.getPlayer(),player -> {});
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        AquaticCratesTesting.getPlayerHandler().savePlayer(CratePlayer.get(e.getPlayer()));
    }

}
