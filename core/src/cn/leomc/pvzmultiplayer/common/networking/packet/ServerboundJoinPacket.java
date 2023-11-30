package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.common.game.GameManager;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import cn.leomc.pvzmultiplayer.common.networking.util.ByteBufUtils;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;
import cn.leomc.pvzmultiplayer.common.server.ServerPlayer;
import cn.leomc.pvzmultiplayer.common.text.component.Component;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public record ServerboundJoinPacket(String name) implements Packet {

    public ServerboundJoinPacket(ByteBuf buf) {
        this(ByteBufUtils.readString(buf));
    }

    @Override
    public void write(ByteBuf buf) {
        ByteBufUtils.writeString(name, buf);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        runLaterServer(() -> {
            if (ServerManager.get().getPlayerList().getPlayer(channel) != null) {
                channel.writeAndFlush(new ClientboundJoinPacket(false, Component.translatable("You are already connected!")));
                return;
            }

            if (ServerManager.get().getPlayerList().getPlayer(name) != null) {
                channel.writeAndFlush(new ClientboundJoinPacket(false, Component.translatable("This name is taken!")));
                channel.close();
                return;
            }
            channel.writeAndFlush(new ClientboundJoinPacket(true, Component.translatable("Success")));
            ServerPlayer player = ServerManager.get().getPlayerList().addPlayer(channel, name);
            player.sendPacket(new ClientboundGameStatePacket(GameManager.get().getState()));
        });

    }
}
