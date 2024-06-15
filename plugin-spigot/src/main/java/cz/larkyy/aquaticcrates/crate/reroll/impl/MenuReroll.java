package cz.larkyy.aquaticcrates.crate.reroll.impl;

import cz.larkyy.aquaticcrates.crate.reroll.Reroll;
import cz.larkyy.aquaticcrates.crate.reroll.RerollManager;
import cz.larkyy.aquaticcrates.crate.reward.Reward;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class MenuReroll extends Reroll {

    public MenuReroll(RerollManager rerollManager, Player player, AtomicReference<Reward> reward, Consumer<Reward> claimConsumer, Consumer<Reward> rerollConsumer) {
        super(rerollManager, player, reward, claimConsumer, rerollConsumer);
    }

    @Override
    public void open() {
        getRerollManager().getCrate().openRerollGUI(this);
    }

    @Override
    public void activate(Event event) {
        if (event instanceof InventoryCloseEvent) {
            if (isRerolling()) {
                getRerollManager().claim(getPlayer());
            }
        }
    }
}
