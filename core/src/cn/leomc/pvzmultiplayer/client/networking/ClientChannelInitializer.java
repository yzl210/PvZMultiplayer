package cn.leomc.pvzmultiplayer.client.networking;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.common.networking.ChannelInitializer;
import cn.leomc.pvzmultiplayer.common.networking.packet.ServerboundJoinPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

public class ClientChannelInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(SocketChannel ch) {
        super.initChannel(ch);
        ch.pipeline().addFirst(new ChannelInboundHandlerAdapter() {
            @Override
            public void channelActive(ChannelHandlerContext ctx) {
                ClientGameManager.get().runLater(() ->
                        ClientGameManager.get().getConnection().sendPacket(new ServerboundJoinPacket(ClientGameManager.get().getName())));
            }
        });
    }
}
