package cn.leomc.pvzmultiplayer.common.game.content.entity.simple;

import cn.leomc.pvzmultiplayer.client.Constants;
import cn.leomc.pvzmultiplayer.client.texture.FixedTextureRegion;
import cn.leomc.pvzmultiplayer.common.game.audio.Sounds;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.zombie.Zombie;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;
import io.netty.buffer.ByteBuf;

public class Pea extends Entity {
    private byte state = 0;

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
        if (position().x > Constants.WIDTH || state >= 8)
            world.removeEntity(this);
        else if (state > 0)
            state++;
    }

    @Override
    public void onCollide(Entity entity) {
        if (state == 0 && entity instanceof Zombie zombie) {
            state = 1;
            velocity.set(0, 0);
            zombie.damage(1);
            ServerManager.get().playSound(Sounds.SPLAT);
        }
    }

    @Override
    public void write(ByteBuf buf) {
        super.write(buf);
        buf.writeByte(state);
    }

    @Override
    public void read(ByteBuf buf) {
        super.read(buf);
        state = buf.readByte();
        if (state > 4)
            texture = new FixedTextureRegion("textures/projectiles/pea.png", 22, 0, 13, 15);
        else if (state > 1)
            texture = new FixedTextureRegion("textures/projectiles/pea.png", 11, 2, 10, 11);

    }

    @Override
    public SimpleEntityType<Pea> type() {
        return SimpleEntities.PEA;
    }
}
