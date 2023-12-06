package cn.leomc.pvzmultiplayer.common.game.content.entity.plants;

import cn.leomc.pvzmultiplayer.client.texture.AnimatedTexture;
import cn.leomc.pvzmultiplayer.client.texture.FixedTexture;
import cn.leomc.pvzmultiplayer.client.texture.Renderable;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityBuilder;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityType;
import com.badlogic.gdx.math.Vector2;

import java.util.EnumMap;
import java.util.function.Supplier;

public interface PlantType<T extends Plant> extends EntityType<T, PlantContext> {

    int sun();

    int seedRechargeTicks();

    Renderable texture(PlantState state);

    static <T extends Plant> PlantBuilder<T> builder() {
        return new PlantBuilder<>();
    }

    class PlantBuilder<T extends Plant> extends EntityBuilder<PlantBuilder<T>, T, PlantType<T>, PlantContext> {
        private int sun;
        private int seedRechargeTicks;
        private final EnumMap<PlantState, Supplier<Renderable>> textures = new EnumMap<>(PlantState.class);

        public PlantBuilder() {
            dimension = new Vector2(80, 80);
        }

        @Override
        public PlantBuilder<T> id(String id) {
            return super.id(id);
        }

        public PlantBuilder<T> sun(int cost) {
            this.sun = cost;
            return this;
        }

        public PlantBuilder<T> seedRechargeTicks(int ticks) {
            this.seedRechargeTicks = ticks;
            return this;
        }

        public PlantBuilder<T> texture(PlantState state, int frames) {
            String path = "plants/" + id + "/" + state.name().toLowerCase() + ".png";
            if (frames < 2)
                return texture(state, () -> FixedTexture.of(path));
            return texture(state, () -> new AnimatedTexture(path, frames));
        }

        public PlantBuilder<T> texture(PlantState state, Supplier<Renderable> texture) {
            this.textures.put(state, texture);
            return this;
        }

        public PlantType<T> build() {
            EntityType<T, PlantContext> type = super.build();
            return new PlantType<>() {
                @Override
                public int sun() {
                    return sun;
                }

                @Override
                public int seedRechargeTicks() {
                    return seedRechargeTicks;
                }

                @Override
                public Renderable texture(PlantState state) {
                    return textures.get(state).get();
                }

                @Override
                public Renderable texture() {
                    return texture(PlantState.IDLE);
                }

                @Override
                public EntityType<T, PlantContext> self() {
                    return type;
                }
            };
        }
    }
}
