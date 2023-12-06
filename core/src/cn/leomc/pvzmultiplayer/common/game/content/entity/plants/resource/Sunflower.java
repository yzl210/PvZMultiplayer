package cn.leomc.pvzmultiplayer.common.game.content.entity.plants.resource;

import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plant;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plants;
import io.netty.buffer.ByteBuf;

public class Sunflower extends Plant {

    public Sunflower(PlantContext context) {
        super(context);
    }

    public Sunflower(ByteBuf buf) {
        super(buf);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public PlantType<?> type() {
        return Plants.SUNFLOWER;
    }
}
