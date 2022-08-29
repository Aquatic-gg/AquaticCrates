package cz.larkyy.aquaticcratestesting.crate;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.animation.Animation;
import cz.larkyy.aquaticcratestesting.api.AquaticCratesAPI;
import cz.larkyy.aquaticcratestesting.animation.AnimationManager;
import cz.larkyy.aquaticcratestesting.crate.inventories.PreviewGUI;
import cz.larkyy.aquaticcratestesting.crate.reroll.RerollManager;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import cz.larkyy.aquaticcratestesting.item.CustomItem;
import cz.larkyy.aquaticcratestesting.player.CratePlayer;
import cz.larkyy.aquaticcratestesting.utils.RewardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Crate {

    private static final NamespacedKey KEY = new NamespacedKey(AquaticCratesTesting.instance(),"CrateIdentifier");

    private final String identifier;
    private final Key key;
    private final String model;
    private final List<Reward> rewards;
    private final boolean requiresCrateToOpen;
    private final AtomicReference<PreviewGUI> previewGUI;
    private final AtomicReference<RerollManager> rerollManager;
    private final AtomicReference<AnimationManager> animationManager;
    private final List<String> hologram;
    private final double hologramYOffset;

    public Crate(String identifier, CustomItem key, String model,
                 List<Reward> rewards, boolean requiresCrateToOpen,
                 AtomicReference<PreviewGUI> previewGUI,
                 AtomicReference<RerollManager> rerollManager,
                 AtomicReference<AnimationManager> animationManager,
                 List<String> hologram,
                 double hologramYOffset) {
        this.identifier = identifier;
        this.key = new Key(key,this);
        this.model = model;
        this.rewards = rewards;
        this.requiresCrateToOpen = requiresCrateToOpen;
        this.previewGUI = previewGUI;
        this.rerollManager = rerollManager;
        this.animationManager = animationManager;
        this.hologram = hologram;
        this.hologramYOffset = hologramYOffset;
    }

    public void openPreview(Player p) {
        if (AquaticCratesTesting.getCrateHandler().isInAnimation(p)) {
            return;
        }
        PreviewGUI gui = previewGUI.get();
        if (gui == null) {
            return;
        }
        gui.open(p,0);
    }

    public PreviewGUI getPreviewGUI() {
        return previewGUI.get();
    }

    public String getIdentifier() {
        return identifier;
    }

    public Key getKey() {
        return key;
    }

    public void giveKey(Player player, int amount, boolean virtual) {
        if (virtual) {
            CratePlayer cp = CratePlayer.get(player);
            cp.addKeys(identifier,amount);
            player.sendMessage("You have been given "+amount+"x "+identifier+" Key");
            player.sendMessage("Now have: "+cp.getKeys(identifier));
        }
        else key.give(Collections.singletonList(player), amount);
    }

    public void giveCrate(Player player) {
        ItemStack is = new ItemStack(Material.CHEST);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§fCrate: "+identifier);
        im.getPersistentDataContainer().set(KEY, PersistentDataType.STRING,identifier);
        is.setItemMeta(im);

        player.getInventory().addItem(is);
    }

    public void giveKeyAll(int amount, boolean virtual) {
        if (virtual) {
            AquaticCratesAPI.getPlayerHandler().loadPlayers();
            AquaticCratesAPI.getPlayerHandler().getPlayers().forEach(
                    p -> p.addKeys(identifier,amount));
        }
        else key.give(new ArrayList<>(Bukkit.getOnlinePlayers()), amount);
    }

    public boolean open(CratePlayer player, PlacedCrate pc, boolean instant) {
        return open(player,pc,instant,true);
    }

    public boolean open(CratePlayer player, PlacedCrate pc, boolean instant, boolean takeKey) {
        if (animationManager.get().isInAnimation(player.getPlayer())) {
            return false;
        }

        if (!animationManager.get().canBeOpened()) {
            player.getPlayer().sendMessage("Someone is already opening this crate!");
            return false;
        }
        if (takeKey && !player.takeKey(key)) {
            player.getPlayer().sendMessage("You do not have the key!");
            return false;
        }
        AtomicReference<Reward> reward = new AtomicReference<>(getRandomReward(player.getPlayer()));
        if (reward.get() == null) {
            player.getPlayer().sendMessage("No available reward has been found! Contact an Admin!");
            return true;
        }

        if (instant) {
            reward.get().give(player.getPlayer());
        } else {
            animationManager.get().open(player.getPlayer(), reward, pc, a-> {
                if (rerollManager.get().setRerolling(player.getPlayer(), reward,
                        r -> {
                            animationManager.get().hideTitle(player.getPlayer());
                            r.give(player.getPlayer());
                            animationManager.get().removeAnimation(player.getPlayer());
                            a.end();
                        },
                        r-> {
                            animationManager.get().hideTitle(player.getPlayer());
                            reward.set(getRandomReward(player.getPlayer()));
                            a.start();
                        })
                ) {
                    animationManager.get().showTitle(animationManager.get().getRerollingTitle(),player.getPlayer());
                }
            });
        }
        return true;
    }

    public Reward getRandomReward(Player p) {
        return RewardUtils.getRandomReward(p,rewards);
    }

    public PlacedCrate spawn(Location location) {
        return AquaticCratesAPI.getCrateHandler().spawnCrate(location,this);
    }

    public String getModel() {
        return model;
    }

    public boolean requiresCrateToOpen() {
        return requiresCrateToOpen;
    }

    public List<Reward> getPossibleRewards(Player p) {
        return RewardUtils.getPossibleRewards(p,rewards);
    }

    public static Crate get(String identifier) {
        return AquaticCratesAPI.getCrate(identifier);
    }

    public static Crate get(ItemStack is) {
        if (is == null) return null;
        ItemMeta im = is.getItemMeta();
        if (im == null) return null;

        String id = im.getPersistentDataContainer().get(KEY,PersistentDataType.STRING);
        if (id == null) return null;
        else return get(id);
    }

    public AtomicReference<AnimationManager> getAnimationManager() {
        return animationManager;
    }

    public List<String> getHologram() {
        return hologram;
    }

    public double getHologramYOffset() {
        return hologramYOffset;
    }
}
