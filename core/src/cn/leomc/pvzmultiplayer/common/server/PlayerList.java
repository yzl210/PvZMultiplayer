package cn.leomc.pvzmultiplayer.common.server;

import cn.leomc.pvzmultiplayer.common.game.GameManager;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import cn.leomc.pvzmultiplayer.common.networking.packet.ClientboundPlayerListPacket;
import com.badlogic.gdx.math.Vector2;
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
    private final Map<ServerPlayer, Vector2> cursors = new HashMap<>();

    public ServerPlayer addPlayer(Channel channel, String name) {
        ServerPlayer player = new ServerPlayer(name, channel);
        players.put(channel, player);
        channelGroup.add(channel);
        syncPlayers();
        GameManager.get().onAddPlayer(player);
        return player;
    }

    public void removePlayer(ServerPlayer player) {
        removePlayer(player.channel());
    }

    public void removePlayer(Channel channel) {
        GameManager.get().onRemovePlayer(getPlayer(channel));
        players.remove(channel);
        cursors.remove(getPlayer(channel));
        channelGroup.remove(channel);
        channel.close();
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

    public Map<ServerPlayer, Vector2> getCursors() {
        return cursors;
    }

    public void setCursor(ServerPlayer player, Vector2 cursor) {
        if (player != null)
            cursors.put(player, cursor);
    }
}
