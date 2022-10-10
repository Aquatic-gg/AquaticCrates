package cz.larkyy.aquaticcratestesting.crate.reroll.impl;

import cz.larkyy.aquaticcratestesting.crate.reroll.Reroll;
import cz.larkyy.aquaticcratestesting.crate.reroll.RerollManager;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class MenuReroll extends Reroll {

    public MenuReroll(RerollManager rerollManager, Player player, AtomicReference<Reward> reward, Consumer<Reward> claimConsumer, Consumer<Reward> rerollConsumer) {
        super(rerollManager, player, reward, claimConsumer, rerollConsumer);
    }

    public void open() {
        getRerollManager().getCrate().openRerollGUI(this);
    }

    @Override
    public void activate(Event e) {

    }
}
