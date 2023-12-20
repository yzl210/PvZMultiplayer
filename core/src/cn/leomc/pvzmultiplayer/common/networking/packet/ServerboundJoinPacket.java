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
        ByteBufUtils.writeString(buf, name);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        runLaterServer(() -> {
            if (ServerManager.get().getPlayerList().getPlayer(channel) != null)
                return;

            if (ServerManager.get().getPlayerList().getPlayer(name) != null) {
                fail(channel, Component.translatable("This name is taken!"));
                return;
            }

            if (!GameManager.get().getState().canJoin()) {
                fail(channel, Component.translatable("The game has already started!"));
                return;
            }

            channel.writeAndFlush(new ClientboundJoinPacket(true, Component.translatable("Success")));
            ServerPlayer player = ServerManager.get().getPlayerList().addPlayer(channel, name);
            player.sendPacket(new ClientboundGameStatePacket(GameManager.get().getState()));
            player.sendPacket(new ClientboundGameSettingsPacket(GameManager.get().getSettings()));
        });
    }

    private void fail(Channel channel, Component reason) {
        channel.writeAndFlush(new ClientboundJoinPacket(false, reason));
        channel.close();
    }

}
