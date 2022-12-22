package cz.larkyy.aquaticcratestesting.animation.task;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import cz.larkyy.aquaticcratestesting.animation.task.impl.*;

import java.util.HashMap;
import java.util.Map;

public class Tasks {

    private final Map<String,Task> taskTypes = new HashMap<>() {
        {
            put("spawnreward",new SpawnRewardTask());
            put("playsound",new PlaySoundTask());
            put("spawnparticle",new SpawnParticleTask());
            put("sendtitle",new SendTitleTask());
            put("movecamera",new CameraMoveTask());
            put("teleportcamera",new CameraTeleportTask());
        }
    };

    public void registerTask(String id, Task task) {
        taskTypes.put(id,task);
    }

    public void unregisterTask(String id) {
        taskTypes.remove(id);
    }

    public Task getTask(String string) {
        return taskTypes.get(string.toLowerCase());
    }

    public static Tasks inst() {
        return AquaticCratesTesting.getTasks();
    }

}
