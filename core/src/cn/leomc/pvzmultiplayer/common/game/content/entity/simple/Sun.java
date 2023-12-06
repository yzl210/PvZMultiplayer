package cn.leomc.pvzmultiplayer.common.game.content.entity.simple;

import cn.leomc.pvzmultiplayer.client.Constants;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityType;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import io.netty.buffer.ByteBuf;

public class Sun extends Entity {

    private int amount;
    private boolean clicked;

    public Sun(EntityCreationContext context) {
        this(context, 25);
    }

    public Sun(EntityCreationContext context, int amount) {
        super(context);
        this.amount = amount;
    }

    public Sun(ByteBuf buf) {
        super(buf);
    }

    @Override
    public void tick() {
        super.tick();

        if (clicked && position().y > Constants.HEIGHT - 50) {
            world.getGameSession().setSun(world.getGameSession().getSun() + amount);
            world.removeEntity(this);
        }
    }

    @Override
    public void onClicked() {
        velocity.set(-0.5f, 0.5f);
        clicked = true;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(amount);
    }

    @Override
    public void read(ByteBuf buf) {
        this.amount = buf.readInt();
    }

    @Override
    public EntityType type() {
        return SimpleEntities.SUN;
    }
}
