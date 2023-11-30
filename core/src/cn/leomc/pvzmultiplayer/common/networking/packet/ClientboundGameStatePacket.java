package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.common.game.GameState;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ClientboundGameStatePacket implements Packet {

    private final GameState state;

    public ClientboundGameStatePacket(GameState state) {
        this.state = state;
    }

    public ClientboundGameStatePacket(ByteBuf buf) {
        this.state = GameState.values()[buf.readInt()];
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
