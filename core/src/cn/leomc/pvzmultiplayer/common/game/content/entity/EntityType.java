package cn.leomc.pvzmultiplayer.common.game.content.entity;

import cn.leomc.pvzmultiplayer.client.texture.Renderable;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import com.badlogic.gdx.math.Vector2;
import io.netty.buffer.ByteBuf;

public interface EntityType<T extends Entity, C extends EntityCreationContext> {
    default String id() {
        return self().id();
    }

    default Renderable texture() {
        return self().texture();
    }

    default boolean canCreate(C context) {
        return self().canCreate(context);
    }

    default T create(C context) {
        return self().create(context);
    }

    default T deserialize(ByteBuf buf) {
        return self().deserialize(buf);
    }

    default Class<? extends T> entityClass() {
        return self().entityClass();
    }

    default boolean hasHealth() {
        return self().hasHealth();
    }

    default double health() {
        return self().health();
    }

    default Vector2 dimension() {
        return self().dimension();
    }

    EntityType<T, C> self();
}