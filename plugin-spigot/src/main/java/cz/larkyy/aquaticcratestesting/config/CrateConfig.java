package cz.larkyy.aquaticcratestesting.config;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.animation.AnimationTitle;
import cz.larkyy.aquaticcratestesting.animation.task.impl.*;
import cz.larkyy.aquaticcratestesting.crate.Crate;
import cz.larkyy.aquaticcratestesting.animation.AnimationManager;
import cz.larkyy.aquaticcratestesting.animation.task.Task;
import cz.larkyy.aquaticcratestesting.animation.task.TaskArgument;
import cz.larkyy.aquaticcratestesting.crate.inventories.PreviewGUI;
import cz.larkyy.aquaticcratestesting.crate.reroll.RerollManager;
import cz.larkyy.aquaticcratestesting.crate.reward.Reward;
import cz.larkyy.aquaticcratestesting.crate.reward.RewardAction;
import cz.larkyy.aquaticcratestesting.item.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.colorutils.Colors;
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
        AtomicReference<RerollManager> rerollManagerAtomicReference = new AtomicReference<>(null);
        AtomicReference<AnimationManager> animationAtomicReference = new AtomicReference<>(null);

        Crate c = new Crate(
                identifier,
                loadKey(),
                getConfiguration().getString("model"),
                loadRewards(),
                getConfiguration().getBoolean("key.requires-crate-to-open",true),
                previewGUIAtomicReference,
                rerollManagerAtomicReference,
                animationAtomicReference,
                loadHologram("hologram"),
                getConfiguration().getDouble("hologram-y-offset",0),
                getConfiguration().getString("open-permission")
        );
        loadPreviewGUI(c,previewGUIAtomicReference);
        loadRerollManager(c,rerollManagerAtomicReference);
        loadAnimationManager(c,animationAtomicReference);
        return c;
    }

    private CustomItem loadKey() {
        return loadItem("key");
    }

    private CustomItem loadItem(String path) {
        List<String> lore = null;
        if (getConfiguration().contains(path+".lore")) {
            lore = getConfiguration().getStringList(path+".lore");
        }

        return CustomItem.create(
                getConfiguration().getString(path+".material","STONE"),
                getConfiguration().getString(path+".display-name"),
                lore,
                getConfiguration().getInt(path+".amount",1),
                getConfiguration().getInt(path+".model-data")
                );
    }

    private List<Reward> loadRewards() {
        List<Reward> list = new ArrayList<>();
        if (!getConfiguration().contains("rewards")) {
            return list;
        }
        getConfiguration().getConfigurationSection("rewards").getKeys(false).forEach(rStr -> {
            list.add(loadReward(rStr));
        });
        return list;
    }

    private Reward loadReward(String id) {
        final String path = "rewards."+id;
        CustomItem item = loadItem(path+".item");
        double chance = getConfiguration().getDouble(path+".chance");
        String permission = getConfiguration().getString(path+".permission");
        boolean giveItem = getConfiguration().getBoolean(path+".give-item",false);
        return new Reward(
                id,
                item,
                chance,
                loadRewardActions(path+".actions"),
                permission,
                giveItem,
                loadHologram(path+".hologram"),
                getConfiguration().getDouble(path+".hologram-y-offset",0)
        );
    }

    private List<RewardAction> loadRewardActions(String path) {
        List<RewardAction> list = new ArrayList<>();
        for (String aStr : getConfiguration().getStringList(path)) {
            RewardAction a = RewardAction.get(aStr);
            if (a == null) continue;
            list.add(a);
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
            title = crate.getIdentifier()+" Preview";
        }
        Menu.Builder builder =Menu.builder(AquaticCratesTesting.instance())
                .size(54)
                .title(title);

        if (getConfiguration().contains("preview.items")) {
            for (String str : getConfiguration().getConfigurationSection("preview.items").getKeys(false)) {
                builder.addItem(loadMenuItem(str,"preview.items."+str));
            }
        }

        atomicReference.set(new PreviewGUI(crate,
                builder,
                getConfiguration().getIntegerList("preview.reward-slots"),
                getConfiguration().getBoolean("preview.openable-by-key",false)
                )
        );
    }

    private MenuItem loadMenuItem(String identifier, String path) {

        CustomItem item = loadItem(path);
        List<Integer> slots;
        if (getConfiguration().contains(path+".slot")) {
            slots = Arrays.asList(getConfiguration().getInt(path+".slot"));
        } else {
            slots = getConfiguration().getIntegerList(path+".slots");
        }

        return MenuItem.builder(identifier,item.getItem())
                .slots(slots)
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
                getConfiguration().getBoolean("animation.use-pumpkin-helmet",false)
                        ));
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

    private List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        if (!getConfiguration().contains("animation.actions")) {
            return tasks;
        }
        getConfiguration().getConfigurationSection("animation.actions").getKeys(false).forEach(id -> {
            switch (getConfiguration().getString("animation.actions."+id+".type").toLowerCase()) {
                case "spawnreward" -> {
                    tasks.add(new SpawnRewardTask(loadArguments("animation.actions." + id, SpawnRewardTask.ARGUMENTS)));
                }
                case "playsound" -> {
                    tasks.add(new PlaySoundTask(loadArguments("animation.actions."+id,PlaySoundTask.ARGUMENTS)));
                }
                case "movecamera" -> {
                    tasks.add(new CameraMoveTask(loadArguments("animation.actions."+id,CameraMoveTask.ARGUMENTS)));
                }
                case "sendtitle" -> {
                    tasks.add(new SendTitleTask(loadArguments("animation.actions."+id,SendTitleTask.ARGUMENTS)));
                }
                case "teleportcamera" -> {
                    tasks.add(new CameraTeleportTask(loadArguments("animation.actions."+id, CameraTeleportTask.ARGUMENTS)));
                }
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
