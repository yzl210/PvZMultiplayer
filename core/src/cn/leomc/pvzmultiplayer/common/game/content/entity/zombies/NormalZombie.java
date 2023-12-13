package cn.leomc.pvzmultiplayer.common.game.content.entity.zombies;

import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.zombie.Zombie;
import cn.leomc.pvzmultiplayer.common.game.content.entity.zombie.ZombieType;
import cn.leomc.pvzmultiplayer.common.game.content.entity.zombie.Zombies;
import io.netty.buffer.ByteBuf;

public class NormalZombie extends Zombie {
    public NormalZombie(EntityCreationContext context) {
        super(context);
    }

    public NormalZombie(ByteBuf buf) {
        super(buf);
    }

    @Override
    public ZombieType<?> type() {
        return Zombies.NORMAL;
    }
}
