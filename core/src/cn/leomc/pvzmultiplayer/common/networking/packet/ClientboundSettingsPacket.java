package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.common.networking.Packet;
import cn.leomc.pvzmultiplayer.common.networking.util.ByteBufUtils;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;
import cn.leomc.pvzmultiplayer.common.server.ServerPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class ClientboundSettingsPacket implements Packet {

    private final List<String> players = new ArrayList<>();


    public ClientboundSettingsPacket() {
        ServerManager.get().getPlayerList().getPlayers().forEach(player -> players.add(player.name()));
    }


    public ClientboundSettingsPacket(ByteBuf buf) {
        int size = buf.readInt();
        for (int i = 0; i < size; i++)
            players.add(ByteBufUtils.readString(buf));

    }

    @Override
    public void write(ByteBuf buf) {
        for (ServerPlayer player : ServerManager.get().getPlayerList().getPlayers()) {
            ByteBufUtils.writeString(player.name(), buf);
        }
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterClient(() -> {

        });
    }
}
