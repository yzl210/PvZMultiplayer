package cn.leomc.pvzmultiplayer.common.game.content.entity.simple;

import cn.leomc.pvzmultiplayer.client.Constants;
import cn.leomc.pvzmultiplayer.common.game.audio.Sounds;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.zombie.Zombie;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;
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
        if (position().x > Constants.WIDTH)
            world.removeEntity(this);
    }

    @Override
    public void onCollide(Entity entity) {
        if (entity instanceof Zombie zombie) {
            world.removeEntity(this);
            zombie.damage(1);
            ServerManager.get().playSound(Sounds.SPLAT);
        }
    }

    @Override
    public SimpleEntityType<Pea> type() {
        return SimpleEntities.PEA;
    }
}
