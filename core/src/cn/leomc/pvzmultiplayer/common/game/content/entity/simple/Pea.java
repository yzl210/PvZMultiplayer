package cn.leomc.pvzmultiplayer.common.game.content.entity.simple;

import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import io.netty.buffer.ByteBuf;

public class Pea extends Entity {

    public Pea(EntityCreationContext context) {
        super(context);
        velocity.set(25, 0);
    }

    public Pea(ByteBuf buf) {
        super(buf);
        velocity.set(25, 0);
    }

    @Override
    public void tick() {
        super.tick();
    }


    @Override
    public SimpleEntityType<Pea> type() {
        return SimpleEntities.PEA;
    }
}
