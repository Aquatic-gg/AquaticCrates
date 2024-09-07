package cz.larkyy.aquaticcrates.crate.price;

import gg.aquatic.aquaticseries.lib.action.ConfiguredAction;
import gg.aquatic.aquaticseries.lib.price.ConfiguredPrice;
import org.bukkit.entity.Player;

import java.util.List;

public class OpenPrice {

    private final ConfiguredPrice<Player> price;
    private final List<ConfiguredAction<Player>> fail;


    public OpenPrice(ConfiguredPrice<Player> price, List<ConfiguredAction<Player>> fail) {
        this.price = price;
        this.fail = fail;
    }

    public boolean has(Player player) {
        if (!price.has(player)) {
            for (ConfiguredAction<Player> playerConfiguredAction : fail) {
                playerConfiguredAction.run(player, ((player1, s) -> s.replace("%player%", player.getName())));
            }
            return false;
        }
        return true;
    }

    public void take(Player player) {
        price.take(player);
    }
}
