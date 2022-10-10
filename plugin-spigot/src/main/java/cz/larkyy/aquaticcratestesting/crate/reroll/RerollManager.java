package cz.larkyy.aquaticcratestesting.crate.reroll;

import cz.larkyy.aquaticcratestesting.api.AquaticCratesAPI;
import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.crate.reroll.impl.InteractionReroll;
import cz.larkyy.aquaticcratestesting.crate.reroll.impl.MenuReroll;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RerollManager {

    private final Map<String,Integer> groups;
    private final Crate crate;

    private final Type type;

    public enum Type {
        INTERACTION,
        GUI
    }

    public RerollManager(Crate crate, Map<String,Integer> groups, Type type) {
        this.crate = crate;
        this.groups = groups.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                            Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new
                        )
                );
        this.type = type;
    }

    public int getPlayerLimit(Player p) {
        for (Map.Entry<String, Integer> entry : groups.entrySet()) {
            String s = entry.getKey();
            int i = entry.getValue();

            if (s.equalsIgnoreCase("default")) {
                return i;
            }
            if (p.hasPermission("aquaticcrates.reroll."+crate.getIdentifier()+"."+s)) {
                return i;
            }
        }
        return 0;
    }

    public void reroll(Player p) {
        Reroll rp = Reroll.get(p);
        if (rp == null) {
            return;
        }
        rp.reroll();
    }

    public boolean setRerolling(Player p, AtomicReference<Reward> reward, Consumer<Reward> claimConsumer, Consumer<Reward> rerollConsumer) {
        Reroll rp = Reroll.get(p);
        if (rp == null) {
            switch (type) {
                case GUI -> {
                    MenuReroll reroll = new MenuReroll(this,p,reward,claimConsumer,rerollConsumer);
                    rp = reroll;
                    reroll.open();
                }
                default -> {
                    rp = new InteractionReroll(this, p, reward, claimConsumer, rerollConsumer);
                }
            }
            AquaticCratesAPI.getPlayerHandler().addRerollPlayer(rp);
        }

        if (rp.getReroll() < getPlayerLimit(p)) {
            rp.setRerolling(true);
            return true;
        }
        claimConsumer.accept(reward.get());
        AquaticCratesAPI.getPlayerHandler().removeRerollPlayer(p);
        return false;
    }

    public void claim(Player p) {
        Reroll rp = Reroll.get(p);
        rp.claim();
        AquaticCratesAPI.getPlayerHandler().removeRerollPlayer(p);
    }

    public Crate getCrate() {
        return crate;
    }
}
