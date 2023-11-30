package cn.leomc.pvzmultiplayer.client.networking;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.common.networking.packet.ServerboundJoinPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientConnectionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ClientGameManager.get().runLater(() ->
                ClientGameManager.get().getConnection().sendPacket(new ServerboundJoinPacket(ClientGameManager.get().getName())));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ClientGameManager.get().disconnect();
    }
}
