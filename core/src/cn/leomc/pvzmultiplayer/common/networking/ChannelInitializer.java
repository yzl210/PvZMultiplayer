package cn.leomc.pvzmultiplayer.common.networking;

import cn.leomc.pvzmultiplayer.common.networking.codec.PacketDecoder;
import cn.leomc.pvzmultiplayer.common.networking.codec.PacketEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ChannelInitializer extends io.netty.channel.ChannelInitializer<SocketChannel> {

    private final ChannelHandler[] handlers;

    public ChannelInitializer(ChannelHandler... handlers) {
        this.handlers = handlers;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        SocketChannelConfig config = ch.config();
        config.setConnectTimeoutMillis(5000);
        config.setKeepAlive(true);

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new PacketEncoder());
        pipeline.addLast(new PacketDecoder());
        pipeline.addLast(new PacketHandler());

        for (ChannelHandler handler : handlers)
            pipeline.addLast(handler);

    }
}
