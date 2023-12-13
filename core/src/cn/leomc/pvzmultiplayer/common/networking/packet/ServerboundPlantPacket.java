package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.common.game.GameManager;
import cn.leomc.pvzmultiplayer.common.game.audio.Sounds;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import cn.leomc.pvzmultiplayer.common.networking.util.ByteBufUtils;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ServerboundPlantPacket implements Packet {

    private final PlantType<?> type;
    private final int x;
    private final int y;

    public ServerboundPlantPacket(PlantType<?> type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public ServerboundPlantPacket(ByteBuf buf) {
        type = (PlantType<?>) EntityManager.get(ByteBufUtils.readString(buf));
        x = buf.readInt();
        y = buf.readInt();
    }

    @Override
    public void write(ByteBuf buf) {
        ByteBufUtils.writeString(type.id(), buf);
        buf.writeInt(x);
        buf.writeInt(y);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterServer(() -> {
            boolean success = GameManager.get().getGameSession().getWorld().plant(x, y, type);
            ctx.writeAndFlush(new ClientboundPlantPacket(success));
            if (success)
                ServerManager.get().playSound(Sounds.PLANT);
        });
    }
}
