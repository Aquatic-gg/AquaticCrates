package cz.larkyy.aquaticcratestesting.config;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.animation.AnimationTitle;
import cz.larkyy.aquaticcratestesting.animation.task.*;
import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.animation.AnimationManager;
import cz.larkyy.aquaticcratestesting.crate.MultiCrate;
import cz.larkyy.aquaticcratestesting.crate.inventories.MultiPreviewGUI;
import cz.larkyy.aquaticcratestesting.crate.inventories.PreviewGUI;
import cz.larkyy.aquaticcratestesting.crate.inventories.RerollGUI;
import cz.larkyy.aquaticcratestesting.crate.milestone.Milestone;
import cz.larkyy.aquaticcratestesting.crate.milestone.MilestoneReward;
import cz.larkyy.aquaticcratestesting.crate.model.ModelAnimation;
import cz.larkyy.aquaticcratestesting.crate.model.ModelAnimations;
import cz.larkyy.aquaticcratestesting.crate.model.ModelSettings;
import cz.larkyy.aquaticcratestesting.crate.price.*;
import cz.larkyy.aquaticcratestesting.crate.reroll.RerollManager;
import cz.larkyy.aquaticcratestesting.crate.reward.ConfiguredRewardAction;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import cz.larkyy.aquaticcratestesting.crate.reward.RewardActions;
import cz.larkyy.aquaticcratestesting.crate.reward.condition.ConfiguredRewardCondition;
import cz.larkyy.aquaticcratestesting.crate.reward.condition.RewardCondition;
import cz.larkyy.aquaticcratestesting.crate.reward.condition.RewardConditions;
import cz.larkyy.aquaticcratestesting.placeholders.Placeholder;
import cz.larkyy.aquaticcratestesting.placeholders.Placeholders;
import cz.larkyy.aquaticcratestesting.utils.IReward;
import cz.larkyy.aquaticcratestesting.utils.colors.Colors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.itemlibrary.CustomItem;
import xyz.larkyy.menulib.Menu;
import xyz.larkyy.menulib.MenuItem;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class CrateConfig extends Config {

    private final String identifier;

    public CrateConfig(JavaPlugin main, File f) {
        super(main, f);
        this.identifier = f.getName().substring(0,f.getName().length()-4);
    }

    public Crate loadCrate() {
        AtomicReference<PreviewGUI> previewGUIAtomicReference = new AtomicReference<>(null);
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
                Colors.format(getConfiguration().getString("display-name",identifier)),
                key,
                loadModelSettings(),
                loadRewards(),
                getConfiguration().getBoolean("key.requires-crate-to-open",true),
                previewGUIAtomicReference,
                rerollGUIAtomicReference,
                rerollManagerAtomicReference,
                animationAtomicReference,
                loadHologram("hologram"),
                getConfiguration().getDouble("hologram-y-offset",0),
                getConfiguration().getString("open-permission"),
                getConfiguration().getBoolean("instant-open-while-sneaking",true),
                loadPriceHandler(),
                loadMilestones(),
                loadRepeatableMilestones(),
                getConfiguration().getInt("hitbox-height",1),
                getConfiguration().getInt("hitbox-width",1)
        );
        loadPreviewGUI(c,previewGUIAtomicReference);
        loadRerollGUI(c,rerollGUIAtomicReference);
        loadRerollManager(c,rerollManagerAtomicReference);
        loadAnimationManager(c,animationAtomicReference);

        c.setBlockType(Material.valueOf(getConfiguration().getString("block-type","BARRIER").toUpperCase()));

        return c;
    }

    private ModelSettings loadModelSettings() {
        String modelId = getConfiguration().getString("model");
        int period = getConfiguration().getInt("idle-animation-period",-1);
        if (!getConfiguration().contains("idle-animations")) {
            return new ModelSettings(modelId, new ModelAnimations(new ArrayList<>(),period));
        }
        List<ModelAnimation> animations = new ArrayList<>();
        for (String key : getConfiguration().getConfigurationSection("idle-animations").getKeys(false)) {
            String animationId = getConfiguration().getString("idle-animations."+key+".animation");
            int animationLength = getConfiguration().getInt("idle-animations."+key+".length");
            animations.add(new ModelAnimation(animationId,animationLength));
        }
        return new ModelSettings(modelId,new ModelAnimations(animations,period));
    }

    private PriceHandler loadPriceHandler() {
        List<PriceGroup> priceGroups = new ArrayList<>();
        if (!getConfiguration().contains("open-prices")) {
            Map<String,Object> args = new HashMap<>();
            args.put("crate",null);
            args.put("amount",1);
            return new PriceHandler(Arrays.asList(new PriceGroup(Arrays.asList(
                    new ConfiguredPrice(
                            OpenPrices.inst().getPriceType("key"),
                            args
                            )
            ))));
        }


        for (String key : getConfiguration().getConfigurationSection("open-prices").getKeys(false)) {
            PriceGroup group = loadPriceGroup("open-prices."+key);
            if (group == null) continue;
            priceGroups.add(group);
        }

        return new PriceHandler(priceGroups);
    }

    private PriceGroup loadPriceGroup(String path) {
        List<ConfiguredPrice> prices = new ArrayList<>();
        for (String key : getConfiguration().getConfigurationSection(path).getKeys(false)) {
            String p = path+"."+key;
            OpenPrice type = OpenPrices.inst().getPriceType(getConfiguration().getString(p+".type"));
            if (type == null) {
                continue;
            }
            prices.add(new ConfiguredPrice(type,loadArguments(p,type.getArgs())));
        }

        PriceGroup group = new PriceGroup(prices);
        return group;
    }

    private CustomItem loadKey() {
        return loadItem("key");
    }

    private CustomItem loadItem(String path) {
        return CustomItem.loadFromYaml(getConfiguration(),path);
    }

    private List<Reward> loadRewards() {
        List<Reward> list = new ArrayList<>();
        if (!getConfiguration().contains("rewards")) {
            return list;
        }
        getConfiguration().getConfigurationSection("rewards").getKeys(false).forEach(rStr -> {
            Reward r = loadReward("rewards."+rStr,rStr);
            if (r != null) {
                if (r.getItem().getItem() == null || r.getPreviewItem().getItem() == null || r.getItem().getItem().getItemMeta() == null || r.getPreviewItem().getItem().getItemMeta() == null) {
                    Bukkit.getConsoleSender().sendMessage("§cReward §l" + rStr + "§c could not be loaded!");
                } else {
                    list.add(r);
                }
            }
        });
        return list;
    }
    private TreeMap<Integer,Milestone> loadMilestones() {
        TreeMap<Integer,Milestone> milestones = new TreeMap<>();
        if (!getConfiguration().contains("milestones")) return milestones;
        for (String key : getConfiguration().getConfigurationSection("milestones").getKeys(false)) {
            String path = "milestones."+key;
            var milestoneRewards = loadMilestoneRewards(path+".rewards");
            var milestone = getConfiguration().getInt(path+".milestone");

            milestones.put(milestone,new Milestone(milestone,milestoneRewards));
        }
        return milestones;
    }
    private HashMap<Integer,Milestone> loadRepeatableMilestones() {
        HashMap<Integer,Milestone> milestones = new HashMap<>();
        if (!getConfiguration().contains("repeatable-milestones")) return milestones;
        for (String key : getConfiguration().getConfigurationSection("repeatable-milestones").getKeys(false)) {
            String path = "repeatable-milestones."+key;
            var milestoneRewards = loadMilestoneRewards(path+".rewards");
            var milestone = getConfiguration().getInt(path+".milestone");

            milestones.put(milestone,new Milestone(milestone,milestoneRewards));
        }
        return milestones;
    }

    private List<IReward> loadMilestoneRewards(String path) {
        var rewards = new ArrayList<IReward>();
        for (String key : getConfiguration().getConfigurationSection(path).getKeys(false)) {
            var reward = loadReward(path+"."+key,key);
            if (reward == null) continue;
            rewards.add(new MilestoneReward(reward,reward.getChance()));
        }
        return rewards;
    }

    private List<ConfiguredRewardCondition> loadRewardConditions(String path) {
        List<ConfiguredRewardCondition> prices = new ArrayList<>();
        if (!getConfiguration().contains(path)) return prices;
        for (String key : getConfiguration().getConfigurationSection(path).getKeys(false)) {
            String p = path+"."+key;
            RewardCondition type = RewardConditions.inst().getPriceType(getConfiguration().getString(p+".type"));
            if (type == null) {
                continue;
            }
            prices.add(new ConfiguredRewardCondition(type,loadArguments(p,type.getArgs())));
        }
        return prices;
    }

    private Reward loadReward(String path, String id) {
        CustomItem item = loadItem(path+".item");
        if (item == null) {
            Bukkit.getConsoleSender().sendMessage("The reward "+path+" could not be loaded, because the item is null!");
            return null;
        }
        CustomItem previewItem = loadItem(path+".preview-item");
        double chance = getConfiguration().getDouble(path+".chance");
        String permission = getConfiguration().getString(path+".permission");
        String model = getConfiguration().getString(path+".model");
        float modelYaw = (float) getConfiguration().getDouble(path+".model-yaw");
        String modelAnimation = getConfiguration().getString(path+".open-animation",null);
        boolean giveItem = getConfiguration().getBoolean(path+".give-item",false);
        return new Reward(
                id,
                item,
                previewItem,
                chance,
                loadRewardActions(path+".actions"),
                permission,
                giveItem,
                loadHologram(path+".hologram"),
                getConfiguration().getDouble(path+".hologram-y-offset",0),
                modelAnimation,
                loadRewardConditions(path+".conditions"),
                model,
                modelYaw
        );
    }

    private List<ConfiguredRewardAction> loadRewardActions(String path) {
        List<ConfiguredRewardAction> list = new ArrayList<>();
        for (String aStr : getConfiguration().getStringList(path)) {
            for (String k : RewardActions.inst().getActionTypes().keySet()) {
                if (aStr.startsWith("[" + k + "]")) {
                    String args = aStr.substring(k.length() + 2).trim();
                    list.add(new ConfiguredRewardAction(RewardActions.inst().getAction(k), args));
                    break;
                }
            }
        }
        return list;
    }

    private List<String> loadHologram(String path) {
        if (!getConfiguration().contains(path)) {
            return new ArrayList<>();
        }
        return getConfiguration().getStringList(path);
    }

    private void loadPreviewGUI(Crate crate,AtomicReference<PreviewGUI> atomicReference) {
        if (!getConfiguration().contains("preview") || !getConfiguration().getBoolean("preview.enabled",true)) {
            return;
        }

        String title;
        if (getConfiguration().contains("preview.title")) {
            title = Colors.format(getConfiguration().getString("preview.title"));
        } else {
            title = crate.getDisplayName()+"§8 Preview";
        }
        Menu.Builder builder =Menu.builder(AquaticCratesTesting.instance())
                .size(getConfiguration().getInt("preview.size",54))
                .title(title);

        if (getConfiguration().contains("preview.items")) {
            for (String str : getConfiguration().getConfigurationSection("preview.items").getKeys(false)) {
                var item = loadMenuItem(str,"preview.items."+str);
                if (item != null) {
                    builder.addItem(item);

                }
            }
        }
        List<String> rewardLore = getConfiguration().getStringList("preview.reward-lore");

        atomicReference.set(new PreviewGUI(crate,
                        builder,
                        getConfiguration().getIntegerList("preview.reward-slots"),
                        rewardLore,
                        getConfiguration().getBoolean("preview.openable-by-key",false)
                )
        );
    }


    private void loadRerollGUI(Crate crate,AtomicReference<RerollGUI> atomicReference) {
        if ((!getConfiguration().contains("reroll") || !getConfiguration().getBoolean("reroll.enabled",true))
                || !getConfiguration().getString("reroll.type","Interaction").equalsIgnoreCase("gui")) {
            return;
        }

        String path = "reroll.gui.";

        String title;
        if (getConfiguration().contains(path+"title")) {
            title = Colors.format(getConfiguration().getString(path+"title"));
        } else {
            title = crate.getDisplayName()+"§8 Reroll";
        }
        Menu.Builder builder =Menu.builder(AquaticCratesTesting.instance())
                .size(getConfiguration().getInt(path+"size",27))
                .title(title);

        if (getConfiguration().contains(path+"items")) {
            for (String str : getConfiguration().getConfigurationSection(path+"items").getKeys(false)) {
                var item = loadMenuItem(str,path+"items."+str);
                if (item != null) {
                    builder.addItem(item);
                }
            }
        }
        int rewardSlot = getConfiguration().getInt(path+"reward-slot",13);

        atomicReference.set(new RerollGUI(
                        builder,
                        rewardSlot
                )
        );
    }

    private MenuItem loadMenuItem(String identifier, String path) {

        CustomItem item = loadItem(path);
        if (item == null) {
            Bukkit.getConsoleSender().sendMessage("§cMenu Item "+path+" could not be loaded, because the item is null!");
            return null;
        }
        List<Integer> slots;
        if (getConfiguration().contains(path+".slot")) {
            slots = Arrays.asList(getConfiguration().getInt(path+".slot"));
        } else {
            slots = getConfiguration().getIntegerList(path+".slots");
        }
        var clickActions = loadRewardActions(path+".click-actions");

        return MenuItem.builder(identifier,item.getItem())
                .slots(slots)
                .action(a -> {
                    clickActions.forEach(action -> {
                        action.run((Player) a.getWhoClicked(),new Placeholders(new Placeholder("%player%",a.getWhoClicked().getName())));
                    });
                })
                .build();
    }

    private void loadRerollManager(Crate c, AtomicReference<RerollManager> atomicReference) {
        if (!getConfiguration().contains("reroll") || !getConfiguration().getBoolean("reroll.enabled",true)) {
            atomicReference.set(new RerollManager(c,new HashMap<>(), RerollManager.Type.INTERACTION));;
        }

        RerollManager.Type type = RerollManager.Type.valueOf(
                getConfiguration().getString("reroll.type","interaction").toUpperCase()
        );
        atomicReference.set(new RerollManager(c,loadRerollGroups(),type));
    }
    private Map<String,Integer> loadRerollGroups() {
        Map<String,Integer> map = new HashMap<>();
        if (getConfiguration().contains("reroll.limits")) {
            getConfiguration().getConfigurationSection("reroll.limits").getKeys(false).forEach(id -> {
                int i = getConfiguration().getInt("reroll.limits."+id);
                map.put(id,i);
            });
        }
        return map;
    }

    private void loadAnimationManager(Crate c, AtomicReference<AnimationManager> atomicReference) {
        AnimationManager.Type type = AnimationManager.Type.valueOf(getConfiguration().getString("animation.type","INSTANT").toUpperCase());
        atomicReference.set(new AnimationManager(
                c,
                type,
                loadTasks(),
                getConfiguration().getInt("animation.length",0),
                loadAnimationTitle("animation.title"),
                loadAnimationTitle("reroll.title"),
                loadLocation("animation.model-location"),
                loadLocation("animation.camera-location"),
                getConfiguration().getBoolean("animation.skippable",false),
                getConfiguration().getBoolean("animation.use-pumpkin-helmet",false),
                getConfiguration().getInt("animation.pre-open.length",0),
                loadPreOpenTitle()
                        ));
    }

    private PreOpenTitle loadPreOpenTitle() {
        if (!getConfiguration().contains("animation.pre-open.title")) {
            return null;
        }
        String path = "animation.pre-open.title";
        String title = getConfiguration().getString(path+".title","");
        String subTitle = getConfiguration().getString(path+".subtitle","");
        int in = getConfiguration().getInt(path+".in",0);
        int stay = getConfiguration().getInt(path+".stay",10);
        int out = getConfiguration().getInt(path+".out",0);

        return new PreOpenTitle(in,out,stay,title,subTitle);
    }

    private AnimationTitle loadAnimationTitle(String path) {
        if (!getConfiguration().contains(path)) {
            return new AnimationTitle(new ArrayList<>(),BarColor.WHITE,BarStyle.SOLID);
        }

        List<String> lines = getConfiguration().getStringList(path+".lines");
        BarColor color = BarColor.valueOf(getConfiguration().getString(path+".color").toUpperCase());
        BarStyle style = BarStyle.valueOf(getConfiguration().getString(path+".style").toUpperCase());

        return new AnimationTitle(lines,color,style);
    }

    private Location loadLocation(String path) {
        if (!getConfiguration().contains(path)) {
            return null;
        }
        World w = Bukkit.getWorld(getConfiguration().getString(path+".world"));
        if (w == null) {
            return null;
        }
        double x = getConfiguration().getDouble(path+".x");
        double y = getConfiguration().getDouble(path+".y");
        double z = getConfiguration().getDouble(path+".z");

        float yaw = Float.parseFloat(getConfiguration().getString(path+".yaw","0"));
        float pitch = Float.parseFloat(getConfiguration().getString(path+".pitch","0"));

        return new Location(w,x,y,z,yaw,pitch);
    }

    private List<ConfiguredTask> loadTasks() {
        List<ConfiguredTask> tasks = new ArrayList<>();
        if (!getConfiguration().contains("animation.actions")) {
            return tasks;
        }
        getConfiguration().getConfigurationSection("animation.actions").getKeys(false).forEach(id -> {
            String actionId = getConfiguration().getString("animation.actions."+id+".type").toLowerCase();
            var task = Tasks.inst().getTask(actionId);
            if (task != null) {
                tasks.add(new ConfiguredTask(task,loadArguments("animation.actions." + id, task.getArgs())));
            }
        });
        return tasks;
    }

    private Map<String,Object> loadArguments(String path, List<TaskArgument> arguments) {
        Map<String,Object> args = new HashMap<>();

        for (TaskArgument arg : arguments) {
            if (getConfiguration().getConfigurationSection(path).getKeys(false).contains(arg.getId())) {
                args.put(arg.getId(),getConfiguration().get(path+"."+arg.getId()));
                continue;
            } else if (arg.isRequired()) {
                Bukkit.getConsoleSender().sendMessage(Colors.format("&cARGUMENT &4"+arg.getId()+" &cIS MISSING, PLEASE UPDATE YOUR CONFIGURATION!"));
            }
            args.put(arg.getId(),arg.getDefaultValue());
        }
        return args;
    }
}
