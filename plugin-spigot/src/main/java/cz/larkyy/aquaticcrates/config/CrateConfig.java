package cz.larkyy.aquaticcrates.config;

import cz.larkyy.aquaticcrates.AquaticCrates;
import cz.larkyy.aquaticcrates.animation.Animation;
import cz.larkyy.aquaticcrates.animation.AnimationTitle;
import cz.larkyy.aquaticcrates.animation.task.*;
import cz.larkyy.aquaticcrates.api.events.CrateInteractEvent;
import cz.larkyy.aquaticcrates.crate.Crate;
import cz.larkyy.aquaticcrates.animation.AnimationManager;
import cz.larkyy.aquaticcrates.crate.PlacedCrate;
import cz.larkyy.aquaticcrates.crate.inventories.RerollGUI;
import cz.larkyy.aquaticcrates.crate.inventories.settings.PreviewGUISettings;
import cz.larkyy.aquaticcrates.crate.milestone.Milestone;
import cz.larkyy.aquaticcrates.crate.milestone.MilestoneReward;
import cz.larkyy.aquaticcrates.crate.model.ModelAnimation;
import cz.larkyy.aquaticcrates.crate.model.ModelAnimations;
import cz.larkyy.aquaticcrates.crate.model.ModelSettings;
import cz.larkyy.aquaticcrates.crate.price.*;
import cz.larkyy.aquaticcrates.crate.price.types.KeyPrice;
import cz.larkyy.aquaticcrates.crate.reroll.RerollManager;
import cz.larkyy.aquaticcrates.crate.reward.Reward;
import cz.larkyy.aquaticcrates.crate.reward.condition.PermissionCondition;
import cz.larkyy.aquaticcrates.menu.Menu;
import cz.larkyy.aquaticcrates.menu.MenuItem;
import gg.aquatic.aquaticseries.lib.ConfigExtKt;
import gg.aquatic.aquaticseries.lib.StringExtKt;
import gg.aquatic.aquaticseries.lib.action.ConfiguredAction;
import gg.aquatic.aquaticseries.lib.action.player.PlayerActionSerializer;
import gg.aquatic.aquaticseries.lib.adapt.AquaticBossBar;
import gg.aquatic.aquaticseries.lib.adapt.AquaticString;
import gg.aquatic.aquaticseries.lib.betterinventory2.serialize.InventorySerializer;
import gg.aquatic.aquaticseries.lib.block.AquaticBlock;
import gg.aquatic.aquaticseries.lib.block.AquaticMultiBlock;
import gg.aquatic.aquaticseries.lib.block.BlockShape;
import gg.aquatic.aquaticseries.lib.block.impl.VanillaBlock;
import gg.aquatic.aquaticseries.lib.chance.IChance;
import gg.aquatic.aquaticseries.lib.format.color.ColorUtils;
import gg.aquatic.aquaticseries.lib.interactable2.AbstractInteractable;
import gg.aquatic.aquaticseries.lib.interactable2.InteractableInteractEvent;
import gg.aquatic.aquaticseries.lib.interactable2.SpawnedInteractable;
import gg.aquatic.aquaticseries.lib.interactable2.base.TempInteractableBase;
import gg.aquatic.aquaticseries.lib.interactable2.impl.block.BlockInteractable;
import gg.aquatic.aquaticseries.lib.interactable2.impl.meg.MegInteractable;
import gg.aquatic.aquaticseries.lib.item.CustomItem;
import gg.aquatic.aquaticseries.lib.price.ConfiguredPrice;
import gg.aquatic.aquaticseries.lib.price.PriceSerializer;
import gg.aquatic.aquaticseries.lib.price.player.PlayerPriceSerializer;
import gg.aquatic.aquaticseries.lib.requirement.ConfiguredRequirement;
import gg.aquatic.aquaticseries.lib.requirement.player.PlayerRequirementSerializer;
import gg.aquatic.aquaticseries.lib.util.AquaticBlockSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

public class CrateConfig extends Config {

    private final String identifier;

    public CrateConfig(JavaPlugin main, File f) {
        super(main, f);
        this.identifier = f.getName().substring(0, f.getName().length() - 4);
    }

    public Crate loadCrate() {
        AtomicReference<RerollGUI> rerollGUIAtomicReference = new AtomicReference<>(null);
        AtomicReference<RerollManager> rerollManagerAtomicReference = new AtomicReference<>(null);
        AtomicReference<AnimationManager> animationAtomicReference = new AtomicReference<>(null);
        var key = loadKey();
        if (key == null) {
            Bukkit.getConsoleSender().sendMessage("§cCrate could not be loaded, because the Key item is null!");
            return null;
        }
        Crate c = new Crate(
                identifier,
                StringExtKt.toAquatic(getConfiguration().getString("display-name", identifier)),
                key,
                loadModelSettings(),
                loadRewards(),
                getConfiguration().getBoolean("key.requires-crate-to-open", true),
                getConfiguration().getBoolean("key.must-be-held", false),
                rerollGUIAtomicReference,
                rerollManagerAtomicReference,
                animationAtomicReference,
                loadHologram("hologram"),
                getConfiguration().getString("open-permission"),
                getConfiguration().getBoolean("instant-open-while-sneaking", true),
                loadPriceHandler(identifier),
                loadMilestones(),
                loadRepeatableMilestones(),
                getConfiguration().getInt("hitbox-height", 1),
                getConfiguration().getInt("hitbox-width", 1),
                loadPreviewGUI(identifier),
                loadInteractable(identifier)
        );
        loadRerollGUI(c, rerollGUIAtomicReference);
        loadRerollManager(c, rerollManagerAtomicReference);
        loadAnimationManager(c, animationAtomicReference);

        c.setBlockType(Material.valueOf(getConfiguration().getString("block-type", "BARRIER").toUpperCase()));

        return c;
    }

    private AbstractInteractable<?> loadInteractable(String crateId) {
        var type = getConfiguration().getString("visual.type", "block").toLowerCase();

        BiConsumer<SpawnedInteractable<?>, InteractableInteractEvent> consumer = (spawned, event) -> {
            event.setCancelled(true);
            var loc = event.getInteractable().getLocation();
            Action action = event.getAction();
            if (action == Action.LEFT_CLICK_AIR) {
                action = Action.LEFT_CLICK_BLOCK;
            } else if (action == Action.RIGHT_CLICK_AIR) {
                action = Action.RIGHT_CLICK_BLOCK;
            }

            PlacedCrate placedCrate = PlacedCrate.get(loc);
            if (placedCrate != null) {
                Bukkit.getPluginManager().callEvent(new CrateInteractEvent(event.getPlayer(), placedCrate, action, loc));
            }
        };

        if (type.equals("modelengine")) {
            return new MegInteractable<>(
                    new TempInteractableBase(),
                    "aquaticcrates_" + crateId,
                    new AquaticMultiBlock(
                            new BlockShape(
                                    new HashMap<>() {
                                        {
                                            put(0, new HashMap<>() {
                                                {
                                                    put(0, "X");
                                                }
                                            });
                                        }
                                    },
                                    new HashMap<>() {
                                        {
                                            put('X', new VanillaBlock(Material.AIR.createBlockData(), null));
                                        }
                                    }
                            )
                    ),
                    getConfiguration().getString("visual.model", ""),
                    consumer
            );
        }
        if (type.equals("multiblock")) {
            var blockSection = getConfiguration().getConfigurationSection("visual.blocks");
            if (blockSection == null) return null;
            var ingredients = new HashMap<Character, AquaticBlock>();
            blockSection.getKeys(false).forEach(key -> {
                var block = AquaticBlockSerializer.INSTANCE.load(Objects.requireNonNull(blockSection.getConfigurationSection(key)));
                ingredients.put(key.charAt(0), block);
            });
            var shapeSection = getConfiguration().getConfigurationSection("visual.layers");
            if (shapeSection == null) return null;
            var layers = new HashMap<Integer, Map<Integer, String>>();
            shapeSection.getKeys(false).forEach(key -> {
                var layer = shapeSection.getConfigurationSection(key);
                var layerMap = new HashMap<Integer, String>();
                layer.getKeys(false).forEach(layerKey -> {
                    var line = layer.getString(layerKey);
                    layerMap.put(Integer.parseInt(layerKey), line);
                });
                layers.put(Integer.parseInt(key), layerMap);
            });
            var shape = new BlockShape(
                    layers,
                    ingredients
            );

            return new BlockInteractable<>(
                    new TempInteractableBase(),
                    "aquaticcrates_" + crateId,
                    new AquaticMultiBlock(
                            shape
                    ),
                    consumer
            );
        }
        var section = getConfiguration().getConfigurationSection("visual");
        AquaticBlock block;
        if (section != null) {
            block = AquaticBlockSerializer.INSTANCE.load(getConfiguration().getConfigurationSection("visual"));
        } else {
            block = new VanillaBlock(Material.STONE.createBlockData(), null);
        }
        return new BlockInteractable<>(
                new TempInteractableBase(),
                "aquaticcrates_" + crateId,
                new AquaticMultiBlock(
                        new BlockShape(
                                new HashMap<>() {
                                    {
                                        put(0, new HashMap<>() {
                                            {
                                                put(0, "X");
                                            }
                                        });
                                    }
                                },
                                new HashMap<>() {
                                    {
                                        put('X', block);
                                    }
                                }
                        )
                ),
                consumer
        );
    }

    private ModelSettings loadModelSettings() {
        String modelId = getConfiguration().getString("model");
        int period = getConfiguration().getInt("idle-animation-period", -1);
        if (!getConfiguration().contains("idle-animations")) {
            return new ModelSettings(modelId, new ModelAnimations(new ArrayList<>(), period));
        }
        List<ModelAnimation> animations = new ArrayList<>();
        for (String key : getConfiguration().getConfigurationSection("idle-animations").getKeys(false)) {
            String animationId = getConfiguration().getString("idle-animations." + key + ".animation");
            int animationLength = getConfiguration().getInt("idle-animations." + key + ".length");
            animations.add(new ModelAnimation(animationId, animationLength));
        }
        return new ModelSettings(modelId, new ModelAnimations(animations, period));
    }

    private PriceHandler loadPriceHandler(String crateId) {
        List<PriceGroup> priceGroups = new ArrayList<>();
        if (!getConfiguration().contains("open-prices")) {
            Map<String, Object> args = new HashMap<>();
            args.put("crate", crateId);
            args.put("amount", 1);
            return new PriceHandler(List.of(new PriceGroup(List.of(
                    new OpenPrice(
                            new ConfiguredPrice<>(new KeyPrice(), args),
                            new ArrayList<>()
                    )
            ))));
        }


        for (String key : getConfiguration().getConfigurationSection("open-prices").getKeys(false)) {
            PriceGroup group = loadPriceGroup("open-prices." + key);
            if (group == null) continue;
            priceGroups.add(group);
        }

        return new PriceHandler(priceGroups);
    }

    private PriceGroup loadPriceGroup(String path) {
        List<OpenPrice> prices = new ArrayList<>();
        for (ConfigurationSection configurationSection : ConfigExtKt.getSectionList(getConfiguration(), path)) {
            var price = PlayerPriceSerializer.INSTANCE.fromSection(configurationSection);
            if (price == null) continue;
            var failActions = PlayerActionSerializer.INSTANCE.fromSections(ConfigExtKt.getSectionList(configurationSection, "fail"));
            prices.add(new OpenPrice(price, failActions));
        }
        return new PriceGroup(prices);
    }

    private CustomItem loadKey() {
        return loadItem("key");
    }

    private CustomItem loadItem(String path) {
        return CustomItem.Companion.loadFromYaml(getConfiguration().getConfigurationSection(path));
    }

    private List<Reward> loadRewards() {
        List<Reward> list = new ArrayList<>();
        if (!getConfiguration().contains("rewards")) {
            return list;
        }
        getConfiguration().getConfigurationSection("rewards").getKeys(false).forEach(rStr -> {
            Reward r = loadReward("rewards." + rStr, rStr);
            if (r != null) {
                if (r.getItem().getItem() == null || r.getItem().getItem().getItemMeta() == null) {
                    Bukkit.getConsoleSender().sendMessage("§cReward §l" + rStr + "§c could not be loaded!");
                } else {
                    list.add(r);
                }
            }
        });
        return list;
    }

    private TreeMap<Integer, Milestone> loadMilestones() {
        TreeMap<Integer, Milestone> milestones = new TreeMap<>();
        if (!getConfiguration().contains("milestones")) return milestones;
        for (String key : getConfiguration().getConfigurationSection("milestones").getKeys(false)) {
            String path = "milestones." + key;
            var milestoneRewards = loadMilestoneRewards(path + ".rewards");
            var milestone = getConfiguration().getInt(path + ".milestone");
            var displayName = getConfiguration().getString(path + ".display-name", "milestone-" + milestone);

            milestones.put(milestone, new Milestone(milestone, milestoneRewards, StringExtKt.toAquatic(displayName)));
        }
        return milestones;
    }

    private HashMap<Integer, Milestone> loadRepeatableMilestones() {
        HashMap<Integer, Milestone> milestones = new HashMap<>();
        if (!getConfiguration().contains("repeatable-milestones")) return milestones;
        for (String key : getConfiguration().getConfigurationSection("repeatable-milestones").getKeys(false)) {
            String path = "repeatable-milestones." + key;
            var milestoneRewards = loadMilestoneRewards(path + ".rewards");
            var milestone = getConfiguration().getInt(path + ".milestone");
            var displayName = getConfiguration().getString(path + ".display-name", "milestone-" + milestone);

            milestones.put(milestone, new Milestone(milestone, milestoneRewards, StringExtKt.toAquatic(displayName)));
        }
        return milestones;
    }

    private List<IChance> loadMilestoneRewards(String path) {
        var rewards = new ArrayList<IChance>();
        for (String key : getConfiguration().getConfigurationSection(path).getKeys(false)) {
            var reward = loadReward(path + "." + key, key);
            if (reward == null) continue;
            rewards.add(new MilestoneReward(reward, reward.getChance()));
        }
        return rewards;
    }

    private List<ConfiguredRequirement<Player>> loadRewardConditions(String path) {
        return PlayerRequirementSerializer.INSTANCE.fromSections(ConfigExtKt.getSectionList(getConfiguration(), path));
    }

    private Reward loadReward(String path, String id) {
        if (AquaticCrates.configDebug)
            Bukkit.getConsoleSender().sendMessage("[AquaticCrates] Loading reward: " + id);
        CustomItem item = loadItem(path + ".item");
        if (item == null) {
            if (AquaticCrates.configDebug)
                Bukkit.getConsoleSender().sendMessage("The reward " + path + " could not be loaded, because the item is null!");
            return null;
        }
        double chance = getConfiguration().getDouble(path + ".chance");
        String permission = getConfiguration().getString(path + ".permission");
        String model = getConfiguration().getString(path + ".model");
        float modelYaw = (float) getConfiguration().getDouble(path + ".model-yaw");
        String modelAnimation = getConfiguration().getString(path + ".open-animation", null);
        boolean giveItem = getConfiguration().getBoolean(path + ".give-item", false);

        List<ConfiguredAction<Player>> actions = new ArrayList<>();
        var conditions = loadRewardConditions(path + ".conditions");
        if (permission != null) {
            conditions.add(new ConfiguredRequirement<>(new PermissionCondition(), new HashMap<>() {
                {
                    put("permission", permission);
                }
            }));
        }

        var displayName = getConfiguration().getString(path + ".display-name");

        var section = getConfiguration().getConfigurationSection(path);

        if (section != null) {
            actions = PlayerActionSerializer.INSTANCE.fromSections(ConfigExtKt.getSectionList(section, "actions"));
        }
        return new Reward(
                id,
                displayName,
                item,
                chance,
                actions,
                giveItem,
                loadAquaticHologram(path + ".hologram"),
                modelAnimation,
                conditions,
                model,
                modelYaw
        );
    }

    private PreviewGUISettings loadPreviewGUI(String crateId) {
        if (!getConfiguration().contains("preview") || !getConfiguration().getBoolean("preview.enabled", true)) {
            return null;
        }

        String title;
        if (getConfiguration().contains("preview.title")) {
            title = (getConfiguration().getString("preview.title"));
        } else {
            title = crateId + " Preview";
        }
        Menu.Builder builder = Menu.builder(AquaticCrates.instance())
                .size(getConfiguration().getInt("preview.size", 54))
                .title(title);

        if (getConfiguration().contains("preview.items")) {
            for (String str : getConfiguration().getConfigurationSection("preview.items").getKeys(false)) {
                var item = loadMenuItem(str, "preview.items." + str);
                if (item != null) {
                    builder.addItem(item);

                }
            }
        }
        List<String> rewardLore = getConfiguration().getStringList("preview.reward-lore");
        List<Integer> rewardSlots = InventorySerializer.INSTANCE.loadSlotSelection(getConfiguration().getStringList("preview.reward-slots")).getSlots().stream().toList();

        var milestonesItem = loadButton("preview.milestones", "milestones");
        String milestoneFormat = getConfiguration().getString("preview.milestones.format", "&7 - &f%milestone% &7(%remains%/%required%)");
        String milestoneFormatReached = getConfiguration().getString("preview.milestones.reached-format", "&7 - &a%milestone% &7(Reached)");
        var openableUsingKey = getConfiguration().getBoolean("preview.openable-using-key", false);
        var clearBottomInventory = getConfiguration().getBoolean("preview.clear-bottom-inventory", false);

        var repeatableMilestonesItem = getConfiguration().contains("repeatable-milestones") ? loadButton("preview.repeatable-milestones", "repeatable-milestones") : null;
        String repeatableMilestoneFormat = getConfiguration().getString("preview.repeatable-milestones.format", "&7 - &f%milestone% &7(%remains%/%required%)");

        var inventorySettings = InventorySerializer.INSTANCE.loadInventory(getConfiguration().getConfigurationSection("preview"));

        //var buttons = loadInventoryButtons(getConfiguration().getConfigurationSection("preview.items"));
        return new PreviewGUISettings(
                inventorySettings,
                rewardSlots,
                milestonesItem,
                milestoneFormat,
                milestoneFormatReached,
                repeatableMilestonesItem,
                repeatableMilestoneFormat,
                rewardLore,
                openableUsingKey,
                clearBottomInventory
        );
    }

    private void loadRerollGUI(Crate crate, AtomicReference<RerollGUI> atomicReference) {
        if ((!getConfiguration().contains("reroll") || !getConfiguration().getBoolean("reroll.enabled", true))
                || !getConfiguration().getString("reroll.type", "Interaction").equalsIgnoreCase("gui")) {
            return;
        }

        String path = "reroll.gui.";

        AquaticString title;
        if (getConfiguration().contains(path + "title")) {
            title = StringExtKt.toAquatic(getConfiguration().getString(path + "title"));
        } else {
            title = StringExtKt.toAquatic(crate.getDisplayName().getString() + " Reroll");
        }
        Menu.Builder builder = Menu.builder(AquaticCrates.instance())
                .size(getConfiguration().getInt(path + "size", 27))
                .title(title.getString());

        if (getConfiguration().contains(path + "items")) {
            for (String str : getConfiguration().getConfigurationSection(path + "items").getKeys(false)) {
                var item = loadMenuItem(str, path + "items." + str);
                if (item != null) {
                    builder.addItem(item);
                }
            }
        }
        int rewardSlot = getConfiguration().getInt(path + "reward-slot", 13);

        atomicReference.set(new RerollGUI(
                        builder,
                        rewardSlot
                )
        );
    }

    private MenuItem loadMenuItem(String identifier, String path) {

        CustomItem item = loadItem(path);
        if (item == null) {
            if (AquaticCrates.configDebug)
                Bukkit.getConsoleSender().sendMessage("§cMenu Item " + path + " could not be loaded, because the item is null!");
            return null;
        }
        List<Integer> slots;
        if (getConfiguration().contains(path + ".slot")) {
            slots = Arrays.asList(getConfiguration().getInt(path + ".slot"));
        } else {
            slots = getConfiguration().getIntegerList(path + ".slots");
        }

        var clickActions = PlayerActionSerializer.INSTANCE.fromSections(ConfigExtKt.getSectionList(getConfiguration(), path + ".click-actions"));

        return MenuItem.builder(identifier, item.getItem())
                .slots(slots)
                .action(a -> {
                    clickActions.forEach(action -> {
                        action.run((Player) a.getWhoClicked(), (p, s) -> s.replace("%player%",a.getWhoClicked().getName()));
                    });
                })
                .build();
    }

    private void loadRerollManager(Crate c, AtomicReference<RerollManager> atomicReference) {
        if (!getConfiguration().contains("reroll") || !getConfiguration().getBoolean("reroll.enabled", true)) {
            atomicReference.set(new RerollManager(c, new HashMap<>(), RerollManager.Type.INTERACTION));
            ;
        }

        RerollManager.Type type = RerollManager.Type.valueOf(
                getConfiguration().getString("reroll.type", "interaction").toUpperCase()
        );
        atomicReference.set(new RerollManager(c, loadRerollGroups(), type));
    }

    private Map<String, Integer> loadRerollGroups() {
        Map<String, Integer> map = new HashMap<>();
        if (getConfiguration().contains("reroll.limits")) {
            getConfiguration().getConfigurationSection("reroll.limits").getKeys(false).forEach(id -> {
                int i = getConfiguration().getInt("reroll.limits." + id);
                map.put(id, i);
            });
        }
        return map;
    }

    private void loadAnimationManager(Crate c, AtomicReference<AnimationManager> atomicReference) {
        AnimationManager.Type type = AnimationManager.Type.valueOf(getConfiguration().getString("animation.type", "INSTANT").toUpperCase());
        var tasks = new TreeMap<Integer, List<ConfiguredAction<Animation>>>();
        if (getConfiguration().contains("animation.actions")) {
            tasks = TaskSerializer.load(getConfiguration().getConfigurationSection("animation.actions"));
        }
        Bukkit.getConsoleSender().sendMessage("Loaded animation tasks: " + tasks.size());

        atomicReference.set(new AnimationManager(
                c,
                type,
                tasks,
                getConfiguration().getInt("animation.length", 0),
                loadAnimationTitle("animation.title"),
                loadAnimationTitle("reroll.title"),
                loadLocation("animation.model-location"),
                loadLocation("animation.camera-location"),
                getConfiguration().getBoolean("animation.skippable", false),
                getConfiguration().getBoolean("animation.use-pumpkin-helmet", false),
                getConfiguration().getInt("animation.pre-open.length", 0),
                loadPreOpenTitle()
        ));
    }

    private PreOpenTitle loadPreOpenTitle() {
        if (!getConfiguration().contains("animation.pre-open.title")) {
            return null;
        }
        String path = "animation.pre-open.title";
        String title = getConfiguration().getString(path + ".title", "");
        String subTitle = getConfiguration().getString(path + ".subtitle", "");
        int in = getConfiguration().getInt(path + ".in", 0);
        int stay = getConfiguration().getInt(path + ".stay", 10);
        int out = getConfiguration().getInt(path + ".out", 0);

        return new PreOpenTitle(in, out, stay, title, subTitle);
    }

    private AnimationTitle loadAnimationTitle(String path) {
        if (!getConfiguration().contains(path)) {
            return new AnimationTitle(new ArrayList<>(), AquaticBossBar.Color.WHITE, AquaticBossBar.Style.SOLID);
        }

        List<String> lines = getConfiguration().getStringList(path + ".lines");
        var color = AquaticBossBar.Color.valueOf(getConfiguration().getString(path + ".color").toUpperCase());
        var style = AquaticBossBar.Style.valueOf(getConfiguration().getString(path + ".style").toUpperCase());

        return new AnimationTitle(lines, color, style);
    }

    private Location loadLocation(String path) {
        if (!getConfiguration().contains(path)) {
            return null;
        }
        World w = Bukkit.getWorld(getConfiguration().getString(path + ".world"));
        if (w == null) {
            return null;
        }
        double x = getConfiguration().getDouble(path + ".x");
        double y = getConfiguration().getDouble(path + ".y");
        double z = getConfiguration().getDouble(path + ".z");

        float yaw = Float.parseFloat(getConfiguration().getString(path + ".yaw", "0"));
        float pitch = Float.parseFloat(getConfiguration().getString(path + ".pitch", "0"));

        return new Location(w, x, y, z, yaw, pitch);
    }

    private Map<String, Object> loadArguments(String path, List<TaskArgument> arguments) {
        Map<String, Object> args = new HashMap<>();

        for (TaskArgument arg : arguments) {
            if (getConfiguration().getConfigurationSection(path).getKeys(false).contains(arg.getId())) {
                args.put(arg.getId(), getConfiguration().get(path + "." + arg.getId()));
                continue;
            } else if (arg.isRequired()) {
                Bukkit.getConsoleSender().sendMessage(ColorUtils.Companion.format("&cARGUMENT &4" + arg.getId() + " &cIS MISSING, PLEASE UPDATE YOUR CONFIGURATION!"));
            }
            args.put(arg.getId(), arg.getDefaultValue());
        }
        return args;
    }
}
