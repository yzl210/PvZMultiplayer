package cn.leomc.pvzmultiplayer.common.networking.packet.world;

import cn.leomc.pvzmultiplayer.common.game.GameManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.zombie.ZombieType;
import cn.leomc.pvzmultiplayer.common.game.logic.competitive.CompetitiveGameSession;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import cn.leomc.pvzmultiplayer.common.networking.packet.ClientboundAddEntityResultPacket;
import cn.leomc.pvzmultiplayer.common.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public record ServerboundSpawnZombiePacket(ZombieType<?> zombie, int lane) implements Packet {

    public ServerboundSpawnZombiePacket(ByteBuf buf) {
        this((ZombieType<?>) EntityManager.get(ByteBufUtils.readString(buf)), buf.readInt());
    }

    @Override
    public void write(ByteBuf buf) {
        ByteBufUtils.writeString(zombie.id(), buf);
        buf.writeInt(lane);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterServer(() -> {
            if (GameManager.get().getGameSession() instanceof CompetitiveGameSession session) {
                boolean success = session.spawnZombie(getPlayer(ctx.channel()), zombie, lane);
                ctx.writeAndFlush(new ClientboundAddEntityResultPacket(success));
            }
        });
    }
}
