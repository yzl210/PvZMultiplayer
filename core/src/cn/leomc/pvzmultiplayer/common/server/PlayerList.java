package cn.leomc.pvzmultiplayer.common.server;

import cn.leomc.pvzmultiplayer.common.networking.Packet;
import cn.leomc.pvzmultiplayer.common.networking.packet.ClientboundPlayerListPacket;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerList {

    private final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final Map<Channel, ServerPlayer> players = new HashMap<>();

    public ServerPlayer addPlayer(Channel channel, String name) {
        channelGroup.add(channel);
        ServerPlayer player = new ServerPlayer(name, channel);
        players.put(channel, player);
        syncPlayers();
        return player;
    }

    public void removePlayer(ServerPlayer player) {
        players.remove(player.channel());
        player.channel().close();
        syncPlayers();
    }

    public ServerPlayer getPlayer(Channel channel) {
        return players.get(channel);
    }

    public ServerPlayer getPlayer(String name) {
        return players.values().stream().filter(player -> player.name().equals(name)).findFirst().orElse(null);
    }

    public List<ServerPlayer> getPlayers() {
        return List.copyOf(players.values());
    }

    public void sendPacket(Packet packet) {
        channelGroup.writeAndFlush(packet);
    }

    public void syncPlayers() {
        sendPacket(new ClientboundPlayerListPacket());
    }
}
