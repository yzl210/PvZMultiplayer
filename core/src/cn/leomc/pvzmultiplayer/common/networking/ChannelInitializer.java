package cn.leomc.pvzmultiplayer.common.networking;

import cn.leomc.pvzmultiplayer.common.networking.codec.PacketDecoder;
import cn.leomc.pvzmultiplayer.common.networking.codec.PacketEncoder;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannelConfig;

public class ChannelInitializer extends io.netty.channel.ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) {
        SocketChannelConfig config = ch.config();
        config.setConnectTimeoutMillis(5000);
        config.setKeepAlive(true);

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new PacketEncoder());
        pipeline.addLast(new PacketDecoder());
        pipeline.addLast(new PacketHandler());
    }
}
