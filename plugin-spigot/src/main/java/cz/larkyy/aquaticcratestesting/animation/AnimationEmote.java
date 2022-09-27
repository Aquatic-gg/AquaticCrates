package cz.larkyy.aquaticcratestesting.animation;

import cz.larkyy.playeremotes.spigotplugin.AquaticPlayerModel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import java.util.HashMap;
import java.util.Map;

public class AnimationEmote {

    private final Location location;
    private final String animation;
    private final Map<Player,AquaticPlayerModel> emotes = new HashMap<>();

    public AnimationEmote(Location location, String animation) {
        this.location = location;
        this.animation = animation;
    }

    private Entity spawnAs() {
        if (location == null) {
            return null;
        }

        return location.getWorld().spawn(location, ArmorStand.class, as -> {
            as.setPersistent(false);
            as.setGravity(false);
            as.setCollidable(false);
            as.setInvisible(true);
        });
    }

    public void play(Player p) {
        if (animation == null) {
            return;
        }

        if (p == null) {
            return;
        }

        if (location == null) {
            return;
        }

        AquaticPlayerModel model = new AquaticPlayerModel(spawnAs(),p);
        emotes.put(p,model);
        model.playAnimation(animation);
    }

    public void despawn(Player p) {
        AquaticPlayerModel model = emotes.get(p);
        if (model == null) {
            return;
        }
        model.getBase().remove();
        model.despawn();
    }
}
