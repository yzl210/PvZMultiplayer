package cn.leomc.pvzmultiplayer.common.game.content.world;

import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import cn.leomc.pvzmultiplayer.client.texture.Renderable;
import cn.leomc.pvzmultiplayer.common.Constants;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityType;
import cn.leomc.pvzmultiplayer.common.networking.util.ByteBufUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    protected double health;

    protected Renderable texture;
    protected float timeElapsed;

    public Entity(EntityCreationContext context) {
        this.id = ID_COUNTER.getAndIncrement();
        this.world = context.world();
        Vector2 dimension = getDimension();
        this.box = new Rectangle(context.position().x, context.position().y, dimension.x, dimension.y);
        this.position = context.position();
        this.health = type().health();
    }

    public Entity(ByteBuf buf) {
        read(buf);
    }

    public void tick() {
        tickCollision();
        position.add(velocity);
        box.setPosition(position);
    }

    public void tickCollision() {
        for (Entity entity : world.getEntities())
            if (entity != this && entity.getLane() == getLane() && box.overlaps(entity.box))
                onCollide(entity);
    }

    public void loadResources() {
        texture = type().texture();
    }

    public void unloadResources() {

    }


    ShapeRenderer shapeRenderer;

    public static boolean debug = true;

    public void render() {
        float interpolation = timeElapsed / Constants.TICK_DURATION;
        float x = position().x + velocity.x * interpolation;
        float y = position().y + velocity.y * interpolation;

        timeElapsed += Gdx.graphics.getDeltaTime();

        Vector2 dimension = getDimension();

        texture.render(x, y, dimension.x, dimension.y);

        if (!debug)
            return;

        if (shapeRenderer == null)
            shapeRenderer = new ShapeRenderer();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(3);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(position.x, position.y, dimension.x, dimension.y);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, dimension.x, dimension.y);
        shapeRenderer.end();

        Gdx.gl.glLineWidth(1);
        SpriteBatch batch = PvZMultiplayerClient.getInstance().getBatch();
        BitmapFont font = PvZMultiplayerClient.getInstance().getFont();

        String text = String.valueOf(id);
        if (type().hasHealth())
            text += " " + health + "/" + type().health();

        batch.begin();
        font.draw(batch, text, x, y + dimension.y + 20);
        batch.end();
    }

    public Vector2 getDimension() {
        return type().dimension();
    }


    public void onCollide(Entity entity) {

    }

    public void damage(double amount) {
        if (!type().hasHealth())
            return;
        health -= amount;
        if (health <= 0)
            world.removeEntity(this);
    }

    public int getLane() {
        return (int) (position.y / Map.DEFAULT.plantGridDimension().y);
    }

    public void write(ByteBuf buf) {
        buf.writeInt(id);
        ByteBufUtils.writeVector2(buf, position);
        ByteBufUtils.writeVector2(buf, velocity);
        if (type().hasHealth())
            buf.writeDouble(health);
    }

    public void read(ByteBuf buf) {
        timeElapsed = 0;
        id = buf.readInt();
        position = ByteBufUtils.readVector2(buf);
        velocity = ByteBufUtils.readVector2(buf);
        if (type().hasHealth())
            health = buf.readDouble();
    }

    public Vector2 velocity() {
        return velocity;
    }

    public Vector2 position() {
        return position;
    }

    public double health() {
        return health;
    }

    public int id() {
        return id;
    }

    public abstract EntityType<?, ?> type();
}
