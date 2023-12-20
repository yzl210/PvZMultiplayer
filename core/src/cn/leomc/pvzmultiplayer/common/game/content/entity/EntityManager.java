package cn.leomc.pvzmultiplayer.common.game.content.entity;

import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plants;
import cn.leomc.pvzmultiplayer.common.game.content.entity.simple.SimpleEntities;
import cn.leomc.pvzmultiplayer.common.game.content.entity.zombie.Zombies;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import cn.leomc.pvzmultiplayer.common.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public class EntityManager {

    private static final Map<String, EntityType<?, ?>> ENTITY_TYPES = new HashMap<>();

    public static <T extends Entity> void register(EntityType<?, ?> type) {
        if (ENTITY_TYPES.containsKey(type.id()))
            throw new IllegalArgumentException("Duplicate entity type id: " + type.id());
        ENTITY_TYPES.put(type.id(), type);
    }

    public static void write(Entity entity, ByteBuf buf) {
        String id = entity.type().id();
        EntityType<?, ?> type = ENTITY_TYPES.get(id);
        if (type == null)
            throw new IllegalArgumentException("Unknown entity type id: " + id);
        ByteBufUtils.writeString(buf, id);
        entity.write(buf);
    }

    public static Entity read(ByteBuf buf) {
        String id = ByteBufUtils.readString(buf);
        EntityType<?, ?> type = ENTITY_TYPES.get(id);
        if (type == null)
            throw new IllegalArgumentException("Unknown entity type id: " + id);
        return type.deserialize(buf);
    }

    public static EntityType<?, ?> get(String id) {
        return ENTITY_TYPES.get(id);
    }

    static {
        Plants.register();
        Zombies.register();
        SimpleEntities.register();
    }

}
