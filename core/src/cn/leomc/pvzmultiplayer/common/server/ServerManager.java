package cn.leomc.pvzmultiplayer.common.server;

import cn.leomc.pvzmultiplayer.common.Constants;
import cn.leomc.pvzmultiplayer.common.game.audio.Audio;
import cn.leomc.pvzmultiplayer.common.networking.ChannelInitializer;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import cn.leomc.pvzmultiplayer.common.networking.packet.ClientboundPlaySoundPacket;
import cn.leomc.pvzmultiplayer.common.server.networking.ServerConnectionHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ServerManager {

    private final Channel serverChannel;
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final PlayerList playerList = new PlayerList();

    public ServerManager() {
        ServerBootstrap b = new ServerBootstrap();
        serverChannel = b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer(new ServerConnectionHandler()))
                .bind(Constants.PORT)
                .syncUninterruptibly()
                .channel();
    }

    public boolean isRunning() {
        return serverChannel != null && serverChannel.isActive();
    }

    public void stop() {
        serverChannel.close();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public void sendPacket(Packet packet) {
        playerList.sendPacket(packet);
    }

    public void playSound(Audio audio) {
        sendPacket(new ClientboundPlaySoundPacket(audio.id()));
    }


    public PlayerList getPlayerList() {
        return playerList;
    }


    public static ServerManager get() {
        return PvZMultiplayerServer.getInstance().getServerManager();
    }
}
