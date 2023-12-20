package cn.leomc.pvzmultiplayer.common.game.content.entity.plants;

import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import io.netty.buffer.ByteBuf;

public abstract class Plant extends Entity {

    protected PlantState state = PlantState.IDLE;


    public Plant(EntityCreationContext context) {
        super(context);
    }

    public Plant(ByteBuf buf) {
        super(buf);
    }

    public PlantState getState() {
        return state;
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void write(ByteBuf buf) {
        super.write(buf);
        buf.writeInt(state.ordinal());
    }

    @Override
    public void read(ByteBuf buf) {
        super.read(buf);
        PlantState newState = PlantState.values()[buf.readInt()];
        if (newState != state) {
            state = newState;
            texture = type().texture(state);
        }
    }

    public abstract PlantType<?> type();
}
