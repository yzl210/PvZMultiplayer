package cn.leomc.pvzmultiplayer.client.networking;

import cn.leomc.pvzmultiplayer.common.Constants;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientConnection {
    private final Channel channel;

    public ClientConnection(String address) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        channel = bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientChannelInitializer())
                .connect(address, Constants.PORT)
                .syncUninterruptibly()
                .channel();
    }

    public boolean isConnected() {
        return channel != null && channel.isActive();
    }

    public void disconnect() {
        channel.disconnect();
        channel.close();
    }

    public Channel getChannel() {
        return channel;
    }

    public void sendPacket(Packet packet) {
        channel.writeAndFlush(packet);
    }

}
