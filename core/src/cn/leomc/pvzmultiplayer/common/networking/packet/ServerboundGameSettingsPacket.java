package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.common.game.GameManager;
import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ServerboundGameSettingsPacket implements Packet {

    private GameSettings gameSettings;

    public ServerboundGameSettingsPacket(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public ServerboundGameSettingsPacket(ByteBuf buf) {
        this(new GameSettings());
        gameSettings.read(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        gameSettings.write(buf);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterServer(() -> GameManager.get().setSettings(gameSettings));
    }
}
