package cz.larkyy.aquaticcratestesting.crate.reroll.impl;

import cz.larkyy.aquaticcratestesting.crate.reroll.Reroll;
import cz.larkyy.aquaticcratestesting.crate.reroll.RerollManager;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class InteractionReroll extends Reroll {
    public InteractionReroll(RerollManager rerollManager, Player player, AtomicReference<Reward> reward, Consumer<Reward> claimConsumer, Consumer<Reward> rerollConsumer) {
        super(rerollManager, player, reward, claimConsumer, rerollConsumer);
    }

    @Override
    public void activate(Event event) {
        if (event instanceof PlayerToggleSneakEvent e) {
            if (e.isSneaking()) {
                getRerollManager().claim(e.getPlayer());
            }
        } else if (event instanceof PlayerInteractEvent e) {
            if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                getRerollManager().reroll(e.getPlayer());
            }
        }
    }

    @Override
    public void open() {

    }
}
