package cn.leomc.pvzmultiplayer.common.networking.packet.world;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.common.game.content.world.World;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class ClientboundUpdateWorldPacket implements Packet {

    private final ByteBuf buf;

    public ClientboundUpdateWorldPacket(World world) {
        this.buf = Unpooled.buffer();
        world.write(buf);
    }

    public ClientboundUpdateWorldPacket(ByteBuf buf) {
        this.buf = buf.readBytes(buf.readableBytes());
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeBytes(this.buf);
        this.buf.readerIndex(0);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterClient(() -> {
            ClientGameManager.get().getWorld().read(buf);
            buf.release();
        });
    }
}
