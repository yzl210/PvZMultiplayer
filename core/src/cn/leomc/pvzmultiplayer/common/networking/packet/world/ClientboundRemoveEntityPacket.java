package cn.leomc.pvzmultiplayer.common.networking.packet.world;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public record ClientboundRemoveEntityPacket(int id) implements Packet {

    public ClientboundRemoveEntityPacket(ByteBuf buf) {
        this(buf.readInt());
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(id);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterClient(() -> ClientGameManager.get().getWorld().removeEntity(id));
    }
}
