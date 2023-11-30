package cn.leomc.pvzmultiplayer.common.networking.codec;

import cn.leomc.pvzmultiplayer.common.networking.Packet;
import cn.leomc.pvzmultiplayer.common.networking.PacketManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) {
        out.writeInt(PacketManager.getId(msg));
        msg.write(out);
    }
}
