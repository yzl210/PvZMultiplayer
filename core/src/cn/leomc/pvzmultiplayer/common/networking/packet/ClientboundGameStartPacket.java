package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.common.game.GameState;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public record ClientboundGameStartPacket() implements Packet {

    public ClientboundGameStartPacket(ByteBuf buf) {
        this();
    }

    @Override
    public void write(ByteBuf buf) {
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterClient(() -> {
            ClientGameManager.get().changeState(GameState.IN_GAME);
        });
    }
}
