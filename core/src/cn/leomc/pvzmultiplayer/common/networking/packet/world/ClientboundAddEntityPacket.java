package cn.leomc.pvzmultiplayer.common.networking.packet.world;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plant;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class ClientboundAddEntityPacket implements Packet {

    private final ByteBuf buf;

    public ClientboundAddEntityPacket(Entity entity) {
        this.buf = Unpooled.buffer();
        EntityManager.write(entity, buf);
    }

    public ClientboundAddEntityPacket(ByteBuf buf) {
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
            Entity entity = EntityManager.read(buf);
            ClientGameManager.get().getWorld().addEntity(entity);
            if (entity instanceof Plant plant)
                ClientGameManager.get().getWorld().setPlant(plant.getColumn(), plant.getRow(), plant);
            buf.release();
        });
    }
}
