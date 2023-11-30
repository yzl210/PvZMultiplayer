package cn.leomc.pvzmultiplayer.common.networking;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.common.server.PvZMultiplayerServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface Packet {
    void write(ByteBuf buf);

    void handle(ChannelHandlerContext ctx);

    default void runLaterServer(Runnable runnable) {
        PvZMultiplayerServer.getInstance().runLater(runnable);
    }

    default void runLaterClient(Runnable runnable) {
        ClientGameManager.get().runLater(runnable);
    }

}
