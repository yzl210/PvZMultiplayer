package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.common.game.GameManager;
import cn.leomc.pvzmultiplayer.common.game.GameState;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public record ServerboundStartGamePacket() implements Packet {

    public ServerboundStartGamePacket(ByteBuf buf) {
        this();
    }

    @Override
    public void write(ByteBuf buf) {
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterServer(() -> {
            if (GameManager.get().getState() == GameState.LOBBY)
                GameManager.get().startGame();
        });
    }
}
