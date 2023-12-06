package cn.leomc.pvzmultiplayer.common.server.networking;

import cn.leomc.pvzmultiplayer.common.server.ServerManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class ServerConnectionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ServerManager.get().getPlayerList().removePlayer(ctx.channel());
    }
}
