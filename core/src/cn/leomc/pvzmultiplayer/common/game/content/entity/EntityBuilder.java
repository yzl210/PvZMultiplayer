package cn.leomc.pvzmultiplayer.common.game.content.entity;

import cn.leomc.pvzmultiplayer.client.texture.Renderable;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import com.badlogic.gdx.math.Vector2;
import io.netty.buffer.ByteBuf;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class EntityBuilder<B extends EntityBuilder<B, E, T, C>, E extends Entity, T extends EntityType<E, C>, C extends EntityCreationContext> {

    protected String id;
    protected Supplier<Renderable> texture;
    protected Predicate<C> canCreate = c -> true;
    protected Function<C, E> constructor;
    protected Function<ByteBuf, E> deserializer;
    protected Class<? extends E> entityClass;
    protected double health = -1;
    protected Vector2 dimension;

    public B id(String id) {
        this.id = id;
        return self();
    }

    public B texture(Supplier<Renderable> texture) {
        this.texture = texture;
        return self();
    }

    public B canCreate(Predicate<C> canCreate) {
        this.canCreate = canCreate;
        return self();
    }

    public B constructor(Function<C, E> constructor) {
        this.constructor = constructor;
        return self();
    }

    public B deserializer(Function<ByteBuf, E> deserializer) {
        this.deserializer = deserializer;
        return self();
    }

    public B entityClass(Class<? extends E> entityClass) {
        this.entityClass = entityClass;
        return self();
    }

    public B health(double health) {
        this.health = health;
        return self();
    }

    public B dimension(Vector2 dimension) {
        this.dimension = dimension;
        return self();
    }

    public B self() {
        return (B) this;
    }

    public EntityType<E, C> build() {
        return new EntityType<>() {
            @Override
            public String id() {
                return id;
            }

            @Override
            public Renderable texture() {
                return texture.get();
            }

            @Override
            public boolean canCreate(C context) {
                return canCreate.test(context);
            }

            @Override
            public E create(C context) {
                return constructor.apply(context);
            }

            @Override
            public E deserialize(ByteBuf buf) {
                return deserializer.apply(buf);
            }

            @Override
            public Class<? extends E> entityClass() {
                return entityClass;
            }

            @Override
            public boolean hasHealth() {
                return health > 0;
            }

            @Override
            public double health() {
                return health;
            }

            @Override
            public Vector2 dimension() {
                return dimension;
            }

            @Override
            public EntityType<E, C> self() {
                return this;
            }
        };
    }
}
