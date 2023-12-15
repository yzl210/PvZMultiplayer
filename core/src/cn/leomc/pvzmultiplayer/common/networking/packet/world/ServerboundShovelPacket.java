package cn.leomc.pvzmultiplayer.common.networking.packet.world;

import cn.leomc.pvzmultiplayer.common.game.GameManager;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public record ServerboundShovelPacket(int x, int y) implements Packet {

    public ServerboundShovelPacket(ByteBuf buf) {
        this(buf.readInt(), buf.readInt());
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterServer(() -> {
            boolean success = GameManager.get().getGameSession().getWorld().shovel(x, y);
            ctx.writeAndFlush(new ClientboundShovelPacket(success));
        });
    }
}
