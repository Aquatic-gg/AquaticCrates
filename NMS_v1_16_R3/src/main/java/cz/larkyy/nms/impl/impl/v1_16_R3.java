package cz.larkyy.nms.impl.impl;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import cz.larkyy.aquaticcratestesting.nms.NMSHandler;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftVector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;

public class v1_16_R3 implements NMSHandler {
    private final Map<Integer, net.minecraft.server.v1_16_R3.Entity> entities = new HashMap<>();

    public int spawnEntity(Location l, Consumer<Entity> factory, List<Player> players, String type) {
        final var entityOpt = EntityTypes.a(type.toLowerCase());
        if (entityOpt.isEmpty()) {
            return -1;
        }

        final var worldServer = ((CraftWorld)l.getWorld()).getHandle();
        final var entity = entityOpt.get().createCreature(
                worldServer,
                null,
                null,
                null,
                new BlockPosition(CraftVector.toNMS(l.toVector())),
                EnumMobSpawn.COMMAND,
                true,
                false
        );

        entity.setPositionRotation(l.getX(),l.getY(),l.getZ(),l.getYaw(),l.getPitch());

        if (factory != null) {
            factory.accept(entity.getBukkitEntity());
            entity.getBukkitEntity();
        }

        final var packetData = new PacketPlayOutEntityMetadata(entity.getId(),entity.getDataWatcher(),true);
        sendPacket(players,entity.P());
        sendPacket(players,packetData);

        if (entity instanceof EntityLiving) {
            EntityLiving livingEntity = (EntityLiving) entity;
            List<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
            for (EnumItemSlot value : EnumItemSlot.values()) {
                list.add(Pair.of(value,livingEntity.getEquipment(value)));
            }
            final var packet = new PacketPlayOutEntityEquipment(entity.getId(),list);
            sendPacket(players,packet);
        }

        entities.put(entity.getId(),entity);
        return entity.getId();
    }

    @Override
    public void despawnEntity(List<Integer> id, List<Player> players) {
        for (int i : id) {
            final var packet = new PacketPlayOutEntityDestroy(i);
            sendPacket(players,packet);
        }
    }

    @Override
    public void updateEntity(int id, Consumer<org.bukkit.entity.Entity> factory) {
        net.minecraft.server.v1_16_R3.Entity entity = entities.get(id);

        if (factory != null) {
            factory.accept(entity.getBukkitEntity());
        }

        final var packetMetadata = new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), true);
        sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),packetMetadata);

        if (entity instanceof EntityLiving) {
            EntityLiving livingEntity = (EntityLiving) entity;
            final List<Pair<EnumItemSlot, ItemStack>> equipmentMap = new ArrayList<>();
            for (EnumItemSlot value : EnumItemSlot.values()) {
                equipmentMap.add(Pair.of(value,livingEntity.getEquipment(value)));
            }
            final var packet = new PacketPlayOutEntityEquipment(entity.getId(),equipmentMap);
            sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),packet);
        }
    }

    @Override
    public void throwEntity(int id, Vector vector) {
        net.minecraft.server.v1_16_R3.Entity entity = entities.get(id);
        entity.getBukkitEntity().setVelocity(vector);
        final var packet = new PacketPlayOutEntityVelocity(id,new Vec3D(vector.getX(),vector.getY(),vector.getZ()));
        sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),packet);
    }

    @Override
    public void teleportEntity(int id, Location location) {
        if (!entities.containsKey(id)) {
            return;
        }
        net.minecraft.server.v1_16_R3.Entity entity = entities.get(id);

        entity.getBukkitEntity().teleport(location);
        final var packet = new PacketPlayOutEntityTeleport(entity);

        sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),packet);
    }

    @Override
    public void moveEntity(int id, Location location) {
        if (!entities.containsKey(id)) {
            return;
        }
        net.minecraft.server.v1_16_R3.Entity entity = entities.get(id);
        Location prevLoc = entity.getBukkitEntity().getLocation();

        entity.getBukkitEntity().teleport(location);
        final var packet = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(
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
                new PacketPlayOutEntityHeadRotation(entities.get(id),(byte) ((int) (location.getYaw() * 256.0F / 360.0F)))
        );
    }

    @Override
    public org.bukkit.entity.Entity getEntity(int id) {
        return entities.get(id).getBukkitEntity();
    }

    @Override
    public void setCamera(int id, Player player) {
        final net.minecraft.server.v1_16_R3.Entity entity;
        if (entities.containsKey(id)) {
            entity = entities.get(id);
        } else {
            entity = ((CraftPlayer)player).getHandle();
        }
        final var packet = new PacketPlayOutCamera(entity);
        sendPacket(Arrays.asList(player),packet);
    }

    @Override
    public void changeGamemode(Player player, GameMode gamemode) {
        final var packet = new PacketPlayOutGameStateChange(new PacketPlayOutGameStateChange.a(3),gamemode.getValue());
        sendPacket(Arrays.asList(player),packet);
    }

    @Override
    public void setPlayerInfo(String action, Player player, String gameMode) {
        final var playerHandle = ((CraftPlayer)player).getHandle();

        PacketPlayOutPlayerInfo.EnumPlayerInfoAction action2 = PacketPlayOutPlayerInfo.EnumPlayerInfoAction.valueOf(action.toUpperCase());
        final var packet = new PacketPlayOutPlayerInfo(action2,playerHandle);

        try {
            final var clazz
                    = Class.forName("net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo.PlayerInfoData");
            final var ctor
                    = clazz.getConstructor(GameProfile.class, int.class, EnumGamemode.class,IChatBaseComponent.class);
            final var obj = ctor.newInstance(
                    playerHandle.getProfile(),
                    playerHandle.ping,
                    EnumGamemode.valueOf(gameMode.toUpperCase()),
                    playerHandle.listName
            );

            final Field packetsField;
            packetsField = packet.getClass().getDeclaredField("b");
            packetsField.setAccessible(true);

            List<Object> list = new ArrayList<>();
            list.add(obj);

            packetsField.set(packet,list);
            sendPacket(Arrays.asList(player), packet);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendPacket(List<Player> players, Packet packet) {
        players.forEach(player -> {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        });
    }
}
