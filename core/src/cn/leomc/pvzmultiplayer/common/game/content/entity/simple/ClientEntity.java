package cn.leomc.pvzmultiplayer.common.game.content.entity.simple;

import cn.leomc.pvzmultiplayer.client.texture.Renderable;
import cn.leomc.pvzmultiplayer.common.Constants;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityType;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import io.netty.buffer.ByteBuf;

public class ClientEntity extends Entity {

    private Renderable texture;
    private Vector2 dimension = new Vector2();
    private EntityRenderer renderer = EntityRenderer.NO_OP;

    public ClientEntity(EntityCreationContext context) {
        super(context);
    }

    public ClientEntity(ByteBuf buf) {
        super(buf);
    }

    @Override
    public void render() {
        renderer.render();
        super.render();
    }

    @Override
    public void clientTick() {
        renderer.tick();
        super.clientTick();
    }

    @Override
    public Renderable getTexture() {
        return texture;
    }

    public void setTexture(Renderable texture) {
        this.texture = texture;
        loadResources();
    }

    @Override
    public Vector2 getDimension() {
        return dimension == null ? Vector2.Zero.cpy() : dimension;
    }

    public void setDimension(Vector2 dimension) {
        this.dimension = dimension;
    }

    public void setRenderer(EntityRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public EntityType<?, ?> type() {
        return SimpleEntities.CLIENT_ENTITY;
    }

    public class DroppedPartRenderer implements EntityRenderer {

        private final int maxTicks;
        private final Vector2 offset;
        private final Interpolation interpolation;
        private int ticks;
        private float timeElapsed;

        public DroppedPartRenderer(int maxTicks, Vector2 offset, Interpolation interpolation) {
            this.maxTicks = maxTicks;
            this.offset = offset;
            this.interpolation = interpolation;
        }

        @Override
        public void tick() {
            ticks++;
            if (ticks >= maxTicks)
                world.removeClientEntity(id);
        }

        @Override
        public void render() {
            float interp = interpolation.apply(timeElapsed / (Constants.TICK_DURATION * maxTicks));
            Vector2 interpolatedOffset = offset.cpy().scl(interp);
            position().add(interpolatedOffset);
            offset.sub(interpolatedOffset);
            timeElapsed += Gdx.graphics.getDeltaTime();
        }
    }

    public interface EntityRenderer {
        EntityRenderer NO_OP = new EntityRenderer() {
            @Override
            public void tick() {
            }

            @Override
            public void render() {
            }
        };

        void tick();

        void render();
    }

}
