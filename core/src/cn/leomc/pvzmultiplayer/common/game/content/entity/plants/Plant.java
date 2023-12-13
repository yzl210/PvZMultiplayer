package cn.leomc.pvzmultiplayer.common.game.content.entity.plants;

import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import io.netty.buffer.ByteBuf;

public abstract class Plant extends Entity {

    public Plant(PlantContext context) {
        super(context);
    }

    public Plant(ByteBuf buf) {
        super(buf);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void write(ByteBuf buf) {
        super.write(buf);
    }

    @Override
    public void read(ByteBuf buf) {
        super.read(buf);
    }

    public abstract PlantType<?> type();
}
