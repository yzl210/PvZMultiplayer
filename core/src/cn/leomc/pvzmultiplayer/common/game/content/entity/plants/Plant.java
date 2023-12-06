package cn.leomc.pvzmultiplayer.common.game.content.entity.plants;

import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import io.netty.buffer.ByteBuf;

public abstract class Plant extends Entity {

    protected double health;

    public Plant(PlantContext context) {
        super(context);
        this.health = type().health();
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
        buf.writeDouble(health);
    }

    @Override
    public void read(ByteBuf buf) {
        super.read(buf);
        health = buf.readDouble();
    }

    public abstract PlantType<?> type();
}
