package cn.leomc.pvzmultiplayer.common.server;

import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.channel.Channel;

public record ServerPlayer(String name, Channel channel) {

    public void sendPacket(Packet packet) {
        channel.writeAndFlush(packet);
    }

}
