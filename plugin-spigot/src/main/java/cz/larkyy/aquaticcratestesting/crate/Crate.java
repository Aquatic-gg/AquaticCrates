package cz.larkyy.aquaticcratestesting.crate;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.api.AquaticCratesAPI;
import cz.larkyy.aquaticcratestesting.animation.AnimationManager;
import cz.larkyy.aquaticcratestesting.api.events.ClaimRewardEvent;
import cz.larkyy.aquaticcratestesting.api.events.CrateOpenEvent;
import cz.larkyy.aquaticcratestesting.crate.inventories.PreviewGUI;
import cz.larkyy.aquaticcratestesting.crate.inventories.RerollGUI;
import cz.larkyy.aquaticcratestesting.crate.price.PriceGroup;
import cz.larkyy.aquaticcratestesting.crate.price.PriceHandler;
import cz.larkyy.aquaticcratestesting.crate.reroll.RerollManager;
import cz.larkyy.aquaticcratestesting.crate.reroll.impl.MenuReroll;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import cz.larkyy.aquaticcratestesting.messages.Messages;
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
import xyz.larkyy.itemlibrary.CustomItem;
import xyz.larkyy.menulib.Menu;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Crate {

    private static final NamespacedKey KEY = new NamespacedKey(AquaticCratesTesting.instance(),"CrateIdentifier");

    private final String identifier;
    private final String displayName;
    private final Key key;
    private final String model;
    private final List<Reward> rewards;

    private final AtomicReference<PreviewGUI> previewGUI;
    private final AtomicReference<RerollGUI> rerollGUI;
    private final AtomicReference<RerollManager> rerollManager;
    private final AtomicReference<AnimationManager> animationManager;
    private final List<String> hologram;
    private final double hologramYOffset;
    private final String permission;
    private final boolean instantWhileSneaking;
    private Material blockType = Material.BARRIER;
    private final PriceHandler priceHandler;

    public Crate(String identifier, String displayName, CustomItem key, String model,
                 List<Reward> rewards, boolean requiresCrateToOpen,
                 AtomicReference<PreviewGUI> previewGUI,
                 AtomicReference<RerollGUI> rerollGUI,
                 AtomicReference<RerollManager> rerollManager,
                 AtomicReference<AnimationManager> animationManager,
                 List<String> hologram,
                 double hologramYOffset, String permission, boolean instantWhileSneaking,
                 PriceHandler priceHandler) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.key = new Key(key,this,requiresCrateToOpen);
        this.model = model;
        this.rewards = rewards;
        this.previewGUI = previewGUI;
        this.rerollGUI = rerollGUI;
        this.rerollManager = rerollManager;
        this.animationManager = animationManager;
        this.hologram = hologram;
        this.hologramYOffset = hologramYOffset;
        this.permission = permission;
        this.instantWhileSneaking = instantWhileSneaking;
        this.priceHandler = priceHandler;
    }

    public PriceHandler getPriceHandler() {
        return priceHandler;
    }

    public void openPreview(Player p, PlacedCrate pc) {
        if (AquaticCratesTesting.getCrateHandler().isInAnimation(p)) {
            return;
        }
        PreviewGUI gui = previewGUI.get();
        if (gui == null) {
            return;
        }
        if (pc == null) {
            if (!gui.isOpenableByKey()) {
                return;
            }
        }
        gui.open(p,0, pc);
    }

    public Material getBlockType() {
        return blockType;
    }

    public void setBlockType(Material blockType) {
        this.blockType = blockType;
    }

    public void openRerollGUI(MenuReroll reroll) {
        RerollGUI gui = rerollGUI.get();
        if (gui == null) {
            return;
        }
        gui.open(reroll);
    }

    public PreviewGUI getPreviewGUI() {
        return previewGUI.get();
    }

    public String getDisplayName() {
        return displayName;
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
            AquaticCratesAPI.getPlayerHandler().loadPlayers(()->{});
            AquaticCratesAPI.getPlayerHandler().getPlayers().forEach(
                    p -> p.addKeys(identifier,amount));
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
            reward = new AtomicReference<>(getRandomReward(player.getPlayer()));
            if (reward.get() == null) {
                Messages.NO_REWARD_AVAILABLE.send(player.getPlayer());
                return;
            }
        }
        var event = new CrateOpenEvent(player.getPlayer(),this);
        if (instant && instantWhileSneaking) {
            var e = new ClaimRewardEvent(player.getPlayer(),reward.get(),this);
            Bukkit.getServer().getPluginManager().callEvent(e);
            reward.get().give(player.getPlayer());
        } else {
            animationManager.get().open(player.getPlayer(), reward, pc, a-> {
                if (rerollManager.get().setRerolling(player.getPlayer(), reward,
                        r -> {
                            animationManager.get().hideTitle(player.getPlayer());
                            var e = new ClaimRewardEvent(player.getPlayer(),r,this);
                            Bukkit.getServer().getPluginManager().callEvent(e);
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
        return RewardUtils.getRandomReward(p,rewards,null);
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public PlacedCrate spawn(Location location) {
        return AquaticCratesAPI.getCrateHandler().spawnCrate(location,this);
    }

    public String getModel() {
        return model;
    }

    public List<Reward> getPossibleRewards(Player p) {
        return RewardUtils.getPossibleRewards(p,rewards);
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

    public List<String> getHologram() {
        return hologram;
    }

    public double getHologramYOffset() {
        return hologramYOffset;
    }
}
