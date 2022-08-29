package cz.larkyy.nms.impl;

import com.mojang.datafixers.util.Pair;
import cz.larkyy.aquaticcratestesting.nms.NMSHandler;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftVector;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

public class v1_19_R2 implements NMSHandler {
    private final Map<Integer, net.minecraft.world.entity.Entity> entities = new HashMap<>();

    public int spawnEntity(Location l, Consumer<org.bukkit.entity.Entity> factory, List<Player> players, String type) {
        final var entityOpt = EntityType.byString(type.toLowerCase());
        if (entityOpt.isEmpty()) {
            return -1;
        }

        final var worldServer = ((CraftWorld)l.getWorld()).getHandle();
        final var entity = entityOpt.get().create(
                worldServer,
                null,
                null,
                null,
                new BlockPos(CraftVector.toNMS(l.toVector())),
                MobSpawnType.COMMAND,
                true,
                false
        );

        entity.absMoveTo(l.getX(),l.getY(),l.getZ(),l.getYaw(),l.getPitch());

        if (factory != null) {
            factory.accept(entity.getBukkitEntity());
            entity.getBukkitEntity();
        }

        final var packetData = new ClientboundSetEntityDataPacket(entity.getId(),entity.getEntityData(),true);
        sendPacket(players,entity.getAddEntityPacket());
        sendPacket(players,packetData);

        if (entity instanceof LivingEntity livingEntity) {
            List<Pair<EquipmentSlot, ItemStack>> list = new ArrayList<>();
            for (EquipmentSlot value : EquipmentSlot.values()) {
                list.add(Pair.of(value,livingEntity.getItemBySlot(value)));
            }
            final var packet = new ClientboundSetEquipmentPacket(entity.getId(),list);
            sendPacket(players,packet);
        }

        entities.put(entity.getId(),entity);
        return entity.getId();
    }

    @Override
    public void despawnEntity(List<Integer> id, List<Player> players) {
        final var packet = new ClientboundRemoveEntitiesPacket(new IntArrayList(id));
        sendPacket(players,packet);
    }

    @Override
    public void updateEntity(int id, Consumer<org.bukkit.entity.Entity> factory) {
        net.minecraft.world.entity.Entity entity = entities.get(id);

        if (factory != null) {
            factory.accept(entity.getBukkitEntity());
        }

        final var packetMetadata = new ClientboundSetEntityDataPacket(entity.getId(), entity.getEntityData(), true);
        sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),packetMetadata);

        if (entity instanceof LivingEntity livingEntity) {
            final List<Pair<EquipmentSlot, ItemStack>> equipmentMap = new ArrayList<>();
            for (EquipmentSlot value : EquipmentSlot.values()) {
                equipmentMap.add(Pair.of(value,livingEntity.getItemBySlot(value)));
            }
            final var packet = new ClientboundSetEquipmentPacket(entity.getId(),equipmentMap);
            sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),packet);
        }
    }

    @Override
    public void throwEntity(int id, Vector vector) {
        net.minecraft.world.entity.Entity entity = entities.get(id);
        entity.getBukkitEntity().setVelocity(vector);
        final var packet = new ClientboundSetEntityMotionPacket(id,new Vec3(vector.getX(),vector.getY(),vector.getZ()));
        sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),packet);
    }

    @Override
    public void teleportEntity(int id, Location location) {
        if (!entities.containsKey(id)) {
            return;
        }
        net.minecraft.world.entity.Entity entity = entities.get(id);

        entity.getBukkitEntity().teleport(location);
        final var packet = new ClientboundTeleportEntityPacket(entity);

        sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),packet);
    }

    @Override
    public void moveEntity(int id, Location location) {
        if (!entities.containsKey(id)) {
            return;
        }
        net.minecraft.world.entity.Entity entity = entities.get(id);
        Location prevLoc = entity.getBukkitEntity().getLocation();

        entity.getBukkitEntity().teleport(location);
        final var packet = new ClientboundMoveEntityPacket.PosRot(
                id,
                (short)((location.getX() * 32 - prevLoc.getX() * 32) * 128),
                (short)((location.getY() * 32 - prevLoc.getY() * 32) * 128),
                (short)((location.getZ() * 32 - prevLoc.getZ() * 32) * 128),
                (byte) ((int) (location.getYaw() * 256.0F / 360.0F)),
                (byte) ((int) (location.getPitch() * 256.0F / 360.0F)),
                true
        );

        sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),packet);
        sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),
                new ClientboundRotateHeadPacket(entities.get(id),(byte) ((int) (location.getYaw() * 256.0F / 360.0F)))
        );
    }

    @Override
    public org.bukkit.entity.Entity getEntity(int id) {
        return entities.get(id).getBukkitEntity();
    }

    @Override
    public void setCamera(int id, Player player) {
        final net.minecraft.world.entity.Entity entity;
        if (entities.containsKey(id)) {
            entity = entities.get(id);
        } else {
            entity = ((CraftPlayer)player).getHandle();
        }
        final var packet = new ClientboundSetCameraPacket(entity);
        sendPacket(Arrays.asList(player),packet);

        final var packet2 = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.UPDATE_GAME_MODE,new ArrayList<>());
    }

    @Override
    public void changeGamemode(Player player, GameMode gamemode) {
        final var packet = new ClientboundGameEventPacket(new ClientboundGameEventPacket.Type(3),gamemode.getValue());
        sendPacket(Arrays.asList(player),packet);
    }

    @Override
    public void setPlayerInfo(String action, Player player, String gameMode) {
        final var playerHandle = ((CraftPlayer)player).getHandle();

        ClientboundPlayerInfoPacket.Action action2 = ClientboundPlayerInfoPacket.Action.valueOf(action.toUpperCase());
        final var packet = new ClientboundPlayerInfoPacket(action2,playerHandle);

        try {
            final Field packetsField;
            packetsField = packet.getClass().getDeclaredField("b");
            packetsField.setAccessible(true);

            List<ClientboundPlayerInfoPacket.PlayerUpdate> list = new ArrayList<>();
            list.add(new ClientboundPlayerInfoPacket.PlayerUpdate(
                    playerHandle.getGameProfile(),
                    playerHandle.latency,
                    GameType.valueOf(gameMode.toUpperCase()),
                    playerHandle.listName,
                    null
                    )
            );

            packetsField.set(packet,list);
            sendPacket(Arrays.asList(player), packet);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendPacket(List<Player> players, Packet packet) {
        players.forEach(player -> {
            ((CraftPlayer)player).getHandle().connection.send(packet);
        });
    }
}
