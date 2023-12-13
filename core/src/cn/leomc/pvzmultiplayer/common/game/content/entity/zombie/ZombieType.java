package cn.leomc.pvzmultiplayer.common.game.content.entity.zombie;

import cn.leomc.pvzmultiplayer.client.texture.AnimatedTexture;
import cn.leomc.pvzmultiplayer.client.texture.FixedTexture;
import cn.leomc.pvzmultiplayer.client.texture.Renderable;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityBuilder;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityType;
import com.badlogic.gdx.math.Vector2;

import java.util.EnumMap;
import java.util.function.Supplier;

public interface ZombieType<T extends Zombie> extends EntityType<T, EntityCreationContext> {

    int sun();

    int eggRechargeTicks();

    double damage();

    Vector2 speed();

    Renderable texture(ZombieState state);

    static <T extends Zombie> ZombieBuilder<T> builder() {
        return new ZombieBuilder<>();
    }

    class ZombieBuilder<T extends Zombie> extends EntityBuilder<ZombieBuilder<T>, T, ZombieType<T>, EntityCreationContext> {
        private int sun;
        private int eggRechargeTicks;
        private double damage;
        private Vector2 speed;
        private final EnumMap<ZombieState, Supplier<Renderable>> textures = new EnumMap<>(ZombieState.class);

        public ZombieBuilder() {
            dimension = new Vector2(80, 160);
        }

        @Override
        public ZombieBuilder<T> id(String id) {
            return super.id(id);
        }

        public ZombieBuilder<T> sun(int cost) {
            this.sun = cost;
            return this;
        }

        public ZombieBuilder<T> eggRechargeTicks(int ticks) {
            this.eggRechargeTicks = ticks;
            return this;
        }

        public ZombieBuilder<T> damage(double damage) {
            this.damage = damage;
            return this;
        }

        public ZombieBuilder<T> speed(Vector2 speed) {
            this.speed = speed;
            return this;
        }

        public ZombieBuilder<T> texture(ZombieState state, int frames) {
            String path = "textures/zombies/" + id + "/" + state.name().toLowerCase() + ".png";
            if (frames < 2)
                return texture(state, () -> FixedTexture.of(path));
            return texture(state, () -> new AnimatedTexture(path, frames));
        }

        public ZombieBuilder<T> texture(ZombieState state, Supplier<Renderable> texture) {
            this.textures.put(state, texture);
            return this;
        }

        public ZombieType<T> build() {
            EntityType<T, EntityCreationContext> type = super.build();
            return new ZombieType<>() {
                @Override
                public int sun() {
                    return sun;
                }

                @Override
                public int eggRechargeTicks() {
                    return eggRechargeTicks;
                }

                @Override
                public double damage() {
                    return damage;
                }

                @Override
                public Vector2 speed() {
                    return speed;
                }

                @Override
                public Renderable texture(ZombieState state) {
                    return textures.get(state).get();
                }

                @Override
                public Renderable texture() {
                    return texture(ZombieState.WALKING);
                }

                @Override
                public EntityType<T, EntityCreationContext> self() {
                    return type;
                }
            };
        }
    }
}
