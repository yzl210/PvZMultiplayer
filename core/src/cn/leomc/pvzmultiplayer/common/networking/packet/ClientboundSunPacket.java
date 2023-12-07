package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public record ClientboundSunPacket(int sun) implements Packet {

    public ClientboundSunPacket(ByteBuf buf) {
        this(buf.readInt());
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(sun);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterClient(() -> ClientGameManager.get().setSun(sun));
    }
}
