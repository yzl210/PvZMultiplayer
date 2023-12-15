package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.common.game.GameState;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public record ClientboundGameStatePacket(GameState state) implements Packet {

    public ClientboundGameStatePacket(ByteBuf buf) {
        this(GameState.values()[buf.readInt()]);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(state.ordinal());
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterClient(() -> ClientGameManager.get().changeState(state));
    }
}
