package cn.leomc.pvzmultiplayer.common.networking.packet.world;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class ClientboundUpdateEntityPacket implements Packet {

    private final ByteBuf buf;

    public ClientboundUpdateEntityPacket(Entity entity) {
        this.buf = Unpooled.buffer();
        buf.writeInt(entity.id());
        entity.write(buf);
    }

    public ClientboundUpdateEntityPacket(ByteBuf buf) {
        this.buf = buf.readBytes(buf.readableBytes());
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeBytes(this.buf);
        this.buf.readerIndex(0);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        if (buf.readableBytes() < 4) {
            buf.release();
            return;
        }
        runLaterClient(() -> {
            int id = buf.readInt();
            Entity entity = ClientGameManager.get().getWorld().getEntity(id);
            if (entity != null)
                entity.read(buf);
            buf.release();
        });
    }
}
