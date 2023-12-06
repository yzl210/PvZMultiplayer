package cn.leomc.pvzmultiplayer.common.game.content.world;

import cn.leomc.pvzmultiplayer.client.texture.Renderable;
import cn.leomc.pvzmultiplayer.common.Constants;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityType;
import cn.leomc.pvzmultiplayer.common.networking.util.ByteBufUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.netty.buffer.ByteBuf;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Entity {

    public static final AtomicInteger ID_COUNTER = new AtomicInteger();

    protected int id;
    protected World world;
    protected Vector2 velocity = new Vector2();
    protected Vector2 position;
    protected Rectangle box;

    protected Renderable texture;
    protected float movementPercentage = 0;

    public Entity(EntityCreationContext context) {
        this.id = ID_COUNTER.getAndIncrement();
        this.world = context.world();
        this.box = new Rectangle(context.position().x, context.position().y, type().dimension().x, type().dimension().y);
        this.position = context.position();
    }

    public Entity(ByteBuf buf) {
        read(buf);
    }

    public void tick() {
        movementPercentage = 0;
        position.add(velocity);
    }

    public void loadResources() {
        texture = type().texture();
    }

    public void unloadResources() {

    }

    public void render() {
        movementPercentage += (float) Constants.TPS / (1 / Gdx.graphics.getDeltaTime());
        float x = position().x + velocity.x * movementPercentage;
        float y = position().y + velocity.y * movementPercentage;

        texture.render(x, y, type().dimension().x, type().dimension().y);
    }


    public void onCollide(Entity entity) {

    }

    public void onClicked() {

    }

    public void write(ByteBuf buf) {
        buf.writeInt(id);
        ByteBufUtils.writeVector2(buf, position);
        ByteBufUtils.writeVector2(buf, velocity);
    }

    public void read(ByteBuf buf) {
        movementPercentage = 0;
        id = buf.readInt();
        position = ByteBufUtils.readVector2(buf);
        velocity = ByteBufUtils.readVector2(buf);
    }

    public Vector2 position() {
        return position.cpy();
    }

    public int id() {
        return id;
    }

    public abstract EntityType<?, ?> type();
}
