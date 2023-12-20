package cn.leomc.pvzmultiplayer.common.game.content.entity.plants.defense;

import cn.leomc.pvzmultiplayer.client.texture.AnimatedTexture;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plant;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plants;
import io.netty.buffer.ByteBuf;

public class Wallnut extends Plant {

    private byte lastState = 0;

    public Wallnut(EntityCreationContext context) {
        super(context);
    }

    public Wallnut(ByteBuf buf) {
        super(buf);
    }

    @Override
    public void read(ByteBuf buf) {
        super.read(buf);

        byte newState;
        if (health < type().health() * (1 / 3f))
            newState = 2;
        else if (health < type().health() * (2 / 3f))
            newState = 1;
        else
            newState = 0;
        updateTexture(lastState, newState);
        lastState = newState;
    }

    public void updateTexture(byte oldState, byte newState) {
        if (oldState == newState)
            return;

        int frame = ((AnimatedTexture) texture).getKeyFrame();

        texture = switch (newState) {
            case 2 -> new AnimatedTexture("textures/plants/wallnut/idle_1_3.png", 5);
            case 1 -> new AnimatedTexture("textures/plants/wallnut/idle_2_3.png", 5);
            default -> new AnimatedTexture("textures/plants/wallnut/idle.png", 5);
        };

        ((AnimatedTexture) texture).setKeyFrame(frame);
    }

    @Override
    public PlantType<?> type() {
        return Plants.WALLNUT;
    }
}
