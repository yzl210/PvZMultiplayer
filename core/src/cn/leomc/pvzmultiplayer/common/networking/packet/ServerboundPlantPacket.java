package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.common.game.GameManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import cn.leomc.pvzmultiplayer.common.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public record ServerboundPlantPacket(PlantType<?> type, int x, int y) implements Packet {

    public ServerboundPlantPacket(ByteBuf buf) {
        this((PlantType<?>) EntityManager.get(ByteBufUtils.readString(buf)), buf.readInt(), buf.readInt());
    }

    @Override
    public void write(ByteBuf buf) {
        ByteBufUtils.writeString(buf, type.id());
        buf.writeInt(x);
        buf.writeInt(y);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterServer(() -> {
            boolean success = GameManager.get().getGameSession().plant(getPlayer(ctx.channel()), x, y, type);
            ctx.writeAndFlush(new ClientboundAddEntityResultPacket(success));
        });
    }
}
