package cn.leomc.pvzmultiplayer.common.networking;

import cn.leomc.pvzmultiplayer.common.networking.packet.*;
import cn.leomc.pvzmultiplayer.common.networking.packet.world.*;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PacketManager {

    private static final Map<Integer, PacketInfo<?>> packetsById = new HashMap<>();
    private static final Map<Class<?>, PacketInfo<?>> packetsByClass = new HashMap<>();


    public static <T extends Packet> void register(Class<T> packetClass, Function<ByteBuf, T> deserializer) {
        if (packetsByClass.containsKey(packetClass))
            throw new IllegalArgumentException("Packet class " + packetClass + " is already registered");
        PacketInfo<T> packetInfo = new PacketInfo<>(packetsById.size(), packetClass, deserializer);
        packetsById.put(packetInfo.id, packetInfo);
        packetsByClass.put(packetClass, packetInfo);
    }

    record PacketInfo<T extends Packet>(int id, Class<T> packetClass, Function<ByteBuf, T> deserializer) {
    }

    public static int getId(Packet packet) {
        PacketInfo<?> packetInfo = packetsByClass.get(packet.getClass());
        if (packetInfo == null)
            throw new IllegalArgumentException("Unknown packet class " + packet.getClass());
        return packetInfo.id();
    }

    public static Packet getPacket(int id, ByteBuf buf) {
        PacketInfo<?> packetInfo = packetsById.get(id);
        if (packetInfo == null)
            throw new IllegalArgumentException("Unknown packet id " + id);
        return packetInfo.deserializer().apply(buf);
    }

    static {
        register(ServerboundJoinPacket.class, ServerboundJoinPacket::new);
        register(ClientboundJoinPacket.class, ClientboundJoinPacket::new);
        register(ClientboundGameStatePacket.class, ClientboundGameStatePacket::new);
        register(ClientboundPlayerListPacket.class, ClientboundPlayerListPacket::new);
        register(ClientboundGameSettingsPacket.class, ClientboundGameSettingsPacket::new);
        register(ServerboundGameSettingsPacket.class, ServerboundGameSettingsPacket::new);
        register(ClientboundGameStartPacket.class, ClientboundGameStartPacket::new);
        register(ServerboundStartGamePacket.class, ServerboundStartGamePacket::new);
        register(ClientboundUpdateWorldPacket.class, ClientboundUpdateWorldPacket::new);
        register(ServerboundPlantPacket.class, ServerboundPlantPacket::new);
        register(ClientboundAddEntityResultPacket.class, ClientboundAddEntityResultPacket::new);
        register(ClientboundAddEntityPacket.class, ClientboundAddEntityPacket::new);
        register(ClientboundRemoveEntityPacket.class, ClientboundRemoveEntityPacket::new);
        register(ClientboundUpdateEntityPacket.class, ClientboundUpdateEntityPacket::new);
        register(ClientboundSunPacket.class, ClientboundSunPacket::new);
        register(ServerboundEntityInteractPacket.class, ServerboundEntityInteractPacket::new);
        register(ClientboundPlaySoundPacket.class, ClientboundPlaySoundPacket::new);
        register(ServerboundShovelPacket.class, ServerboundShovelPacket::new);
        register(ClientboundShovelPacket.class, ClientboundShovelPacket::new);
        register(ClientboundEntityCooldownsPacket.class, ClientboundEntityCooldownsPacket::new);
        register(ServerboundSpawnZombiePacket.class, ServerboundSpawnZombiePacket::new);
        register(ClientboundCursorPositionPacket.class, ClientboundCursorPositionPacket::new);
        register(ServerboundCursorPositionPacket.class, ServerboundCursorPositionPacket::new);
    }

}
