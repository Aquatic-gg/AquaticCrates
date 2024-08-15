package cz.larkyy.aquaticcrates.crate;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.api.AquaticCratesAPI;
import cz.larkyy.aquaticcrates.animation.AnimationManager;
import cz.larkyy.aquaticcrates.api.events.ClaimRewardEvent;
import cz.larkyy.aquaticcrates.api.events.CrateOpenEvent;
import cz.larkyy.aquaticcrates.api.events.KeyUseEvent;
import cz.larkyy.aquaticcrates.crate.inventories.PreviewGUI;
import cz.larkyy.aquaticcrates.crate.inventories.RerollGUI;
import cz.larkyy.aquaticcrates.crate.inventories.settings.PreviewGUISettings;
import cz.larkyy.aquaticcrates.crate.milestone.Milestone;
import cz.larkyy.aquaticcrates.crate.milestone.MilestoneHandler;
import cz.larkyy.aquaticcrates.crate.model.ModelSettings;
import cz.larkyy.aquaticcrates.crate.price.PriceGroup;
import cz.larkyy.aquaticcrates.crate.price.PriceHandler;
import cz.larkyy.aquaticcrates.crate.reroll.RerollManager;
import cz.larkyy.aquaticcrates.crate.reroll.impl.MenuReroll;
import cz.larkyy.aquaticcrates.crate.reward.Reward;
import cz.larkyy.aquaticcrates.messages.Messages;
import cz.larkyy.aquaticcrates.player.CratePlayer;
import cz.larkyy.aquaticcrates.utils.RewardUtils;
import gg.aquatic.aquaticseries.lib.adapt.AquaticString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.larkyy.itemlibrary.CustomItem;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Crate extends CrateBase {

    private static final NamespacedKey KEY = new NamespacedKey(AquaticCrates.instance(),"CrateIdentifier");

    private final Key key;
    private final List<Reward> rewards;

    private final AtomicReference<RerollGUI> rerollGUI;
    private final AtomicReference<RerollManager> rerollManager;
    private final AtomicReference<AnimationManager> animationManager;
    private final String permission;
    private final boolean instantWhileSneaking;
    private final PriceHandler priceHandler;
    private final MilestoneHandler milestoneHandler;
    private final PreviewGUISettings previewGUISettings;

    public Crate(String identifier, AquaticString displayName, CustomItem key, ModelSettings modelSettings,
                 List<Reward> rewards, boolean requiresCrateToOpen, boolean mustBeHeld,
                 AtomicReference<RerollGUI> rerollGUI,
                 AtomicReference<RerollManager> rerollManager,
                 AtomicReference<AnimationManager> animationManager,
                 List<String> hologram,
                 double hologramYOffset, String permission, boolean instantWhileSneaking,
                 PriceHandler priceHandler, TreeMap<Integer, Milestone> milestones,
                 HashMap<Integer,Milestone> repeatableMilestones, int hitboxHeight,
                 int hitboxWidth, PreviewGUISettings previewGUISettings) {
        super(identifier,displayName,modelSettings,hologram,hologramYOffset,hitboxHeight,hitboxWidth);
        this.previewGUISettings = previewGUISettings;
        this.key = new Key(key,this,requiresCrateToOpen, mustBeHeld);
        this.rewards = rewards;
        this.rerollGUI = rerollGUI;
        this.rerollManager = rerollManager;
        this.animationManager = animationManager;
        this.permission = permission;
        this.instantWhileSneaking = instantWhileSneaking;
        this.priceHandler = priceHandler;
        this.milestoneHandler = new MilestoneHandler(this,milestones,repeatableMilestones);
    }

    public MilestoneHandler getMilestoneHandler() {
        return milestoneHandler;
    }

    public PriceHandler getPriceHandler() {
        return priceHandler;
    }

    public PreviewGUISettings getPreviewGUISettings() {
        return previewGUISettings;
    }

    public void openPreview(Player p, PlacedCrate pc) {
        if (AquaticCrates.getCrateHandler().isInAnimation(p)) {
            return;
        }
        var gui = new PreviewGUI(this, p);
        if (pc == null) {
            if (!gui.isOpenableByKey()) {
                return;
            }
        }
        gui.open(p,pc);
    }

    public void openRerollGUI(MenuReroll reroll) {
        RerollGUI gui = rerollGUI.get();
        if (gui == null) {
            return;
        }
        gui.open(reroll);
    }

    public Key getKey() {
        return key;
    }

    public void giveKey(Player player, int amount, boolean virtual) {
        if (virtual) {
            CratePlayer cp = CratePlayer.get(player);
            cp.addKeys(getIdentifier(),amount);
        }
        else key.give(Collections.singletonList(player), amount);
    }

    public void giveCrate(Player player) {
        ItemStack is = new ItemStack(Material.CHEST);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§fCrate: "+getIdentifier());
        im.getPersistentDataContainer().set(KEY, PersistentDataType.STRING,getIdentifier());
        is.setItemMeta(im);

        player.getInventory().addItem(is);
    }

    public void giveKeyAll(int amount, boolean virtual) {
        if (virtual) {
            AquaticCratesAPI.getPlayerHandler().loadPlayers(()->{});
            AquaticCratesAPI.getPlayerHandler().getPlayers().forEach(
                    p -> p.addKeys(getIdentifier(),amount));
        }
        else key.give(new ArrayList<>(Bukkit.getOnlinePlayers()), amount);
    }

    public void open(CratePlayer player, PlacedCrate pc, boolean instant) {
        open(player,pc,instant,true);
    }

    public void open(CratePlayer player, PlacedCrate pc, boolean instant, boolean takeKey) {
        if (AquaticCratesAPI.getCrateHandler().isInAnimation(player.getPlayer())) {
            return;
        }

        if (permission != null && !player.getPlayer().hasPermission(permission)) {
            Messages.CRATE_NO_PERMISSION.send(player.getPlayer());
            return;
        }

        if (!animationManager.get().canBeOpened(player.getPlayer())) {
            return;
        }
        AtomicReference<Reward> reward;
        if (takeKey) {
            PriceGroup pg = priceHandler.chooseGroup(player.getPlayer(),this);
            if (pg == null) {
                Messages.DO_NOT_HAVE_KEY.send(player.getPlayer());
                return;
            }
            if (!pg.has(player.getPlayer(),this)) {
                Messages.DO_NOT_HAVE_KEY.send(player.getPlayer());
                return;
            }
            reward = new AtomicReference<>(getRandomReward(player.getPlayer()));
            if (reward.get() == null) {
                Messages.NO_REWARD_AVAILABLE.send(player.getPlayer());
                return;
            }

            pg.take(player.getPlayer(),this);
        }
        else {
            var event = new KeyUseEvent(player.getPlayer(),this, KeyUseEvent.KeyType.NONE,null);
            Bukkit.getPluginManager().callEvent(event);
            reward = new AtomicReference<>(getRandomReward(player.getPlayer()));
            if (reward.get() == null) {
                Messages.NO_REWARD_AVAILABLE.send(player.getPlayer());
                return;
            }
        }
        var event = new CrateOpenEvent(player.getPlayer(),this);
        if (instant && instantWhileSneaking) {
            var e = new ClaimRewardEvent(player.getPlayer(),reward.get(),this);
            milestoneHandler.increaseAmt(player.getPlayer());
            Bukkit.getServer().getPluginManager().callEvent(e);
            reward.get().give(player.getPlayer());
        } else {
            animationManager.get().open(player.getPlayer(), reward, pc, a-> {
                if (rerollManager.get().setRerolling(player.getPlayer(), reward,
                        r -> {
                            animationManager.get().hideTitle(player.getPlayer());
                            var e = new ClaimRewardEvent(player.getPlayer(),r,this);
                            Bukkit.getServer().getPluginManager().callEvent(e);
                            milestoneHandler.increaseAmt(player.getPlayer());
                            r.give(player.getPlayer());
                            animationManager.get().removeAnimation(player.getPlayer());
                            a.end();
                        },
                        r -> {
                            animationManager.get().hideTitle(player.getPlayer());
                            reward.set(getRandomReward(player.getPlayer()));
                            a.start();
                        })
                ) {
                    animationManager.get().showTitle(animationManager.get().getRerollingTitle(),player.getPlayer());
                }
            });
        }
        Bukkit.getPluginManager().callEvent(event);
    }

    public Reward getRandomReward(Player p) {
        var rewards = RewardUtils
                .getPossibleRewards(p,this.rewards,this);
        return (Reward) RewardUtils.getRandomReward(rewards,null);
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public PlacedCrate spawn(Location location) {
        return AquaticCratesAPI.getCrateHandler().spawnCrate(location,this);
    }


    public List<Reward> getPossibleRewards(Player p) {
        return RewardUtils.getPossibleRewards(p,rewards,this).stream().map(r -> (Reward)r).toList();
    }

    public RerollManager getRerollManager() {
        return rerollManager.get();
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

}
