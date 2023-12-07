package cn.leomc.pvzmultiplayer.common.game.content.entity.simple;

import cn.leomc.pvzmultiplayer.client.Constants;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import cn.leomc.pvzmultiplayer.common.game.content.world.Interactable;
import io.netty.buffer.ByteBuf;

public class Sun extends Entity implements Interactable {

    private int amount;
    private float maxDrop;
    private boolean clicked;
    private float dropped;

    public Sun(EntityCreationContext context) {
        this(context, 60f);
    }

    public Sun(EntityCreationContext context, float maxDrop) {
        this(context, 25, maxDrop);
    }

    public Sun(EntityCreationContext context, int amount, float maxDrop) {
        super(context);
        this.amount = amount;
        this.maxDrop = maxDrop;
    }

    public Sun(ByteBuf buf) {
        super(buf);
    }

    @Override
    public void tick() {
        super.tick();
        if (!clicked) {
            dropped -= velocity.y;
            if (dropped >= maxDrop)
                velocity.set(0, 0);
        }

        if (clicked && position().y > Constants.HEIGHT - 50) {
            world.getGameSession().setSun(world.getGameSession().getSun() + amount);
            world.removeEntity(this);
        }
    }


    @Override
    public void onTouchDown(int x, int y, int pointer, int button) {
        clicked = true;
        velocity.set(-(position.x / 20f), (Constants.HEIGHT - 50 - position.y) / 20f);
    }

    @Override
    public void write(ByteBuf buf) {
        super.write(buf);
        buf.writeInt(amount);
    }

    @Override
    public void read(ByteBuf buf) {
        super.read(buf);
        this.amount = buf.readInt();
    }

    @Override
    public SimpleEntityType<Sun> type() {
        return SimpleEntities.SUN;
    }
}
