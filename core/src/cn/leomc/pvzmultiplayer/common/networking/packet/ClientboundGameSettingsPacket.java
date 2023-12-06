package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ClientboundGameSettingsPacket implements Packet {

    private final GameSettings settings;


    public ClientboundGameSettingsPacket(GameSettings settings) {
        this.settings = settings;
    }


    public ClientboundGameSettingsPacket(ByteBuf buf) {
        GameSettings.GameMode mode = GameSettings.GameMode.values()[buf.readInt()];
        settings = mode.read(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(settings.mode().ordinal());
        settings.write(buf);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterClient(() -> {
            ClientGameManager.get().setGameSettings(settings);
            ClientGameManager.get().reloadGameSettings();
//            PvZMultiplayerClient.getInstance().runLater(() -> {
//                if (SceneManager.get().getCurrentScene() instanceof LobbyScene scene)
//                    scene.refreshSettings();
//            });
        });
    }
}
