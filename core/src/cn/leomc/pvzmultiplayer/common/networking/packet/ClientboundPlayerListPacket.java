package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import cn.leomc.pvzmultiplayer.client.SceneManager;
import cn.leomc.pvzmultiplayer.client.scene.LobbyScene;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import cn.leomc.pvzmultiplayer.common.networking.util.ByteBufUtils;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class ClientboundPlayerListPacket implements Packet {

    private final List<String> players = new ArrayList<>();


    public ClientboundPlayerListPacket() {
        ServerManager.get().getPlayerList().getPlayers().forEach(player -> players.add(player.name()));
    }

    public ClientboundPlayerListPacket(ByteBuf buf) {
        int size = buf.readInt();
        for (int i = 0; i < size; i++)
            players.add(ByteBufUtils.readString(buf));
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(players.size());
        players.forEach(player -> ByteBufUtils.writeString(player, buf));
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterClient(() -> {
            ClientGameManager.get().setPlayerList(players);
            PvZMultiplayerClient.getInstance().runLater(() -> {
                if (SceneManager.get().getCurrentScene() instanceof LobbyScene scene)
                    scene.refresh();
            });
        });
    }
}
