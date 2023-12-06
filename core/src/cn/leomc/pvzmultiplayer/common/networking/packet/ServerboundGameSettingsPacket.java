package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.common.game.GameManager;
import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ServerboundGameSettingsPacket implements Packet {

    private final GameSettings settings;


    public ServerboundGameSettingsPacket(GameSettings settings) {
        this.settings = settings;
    }


    public ServerboundGameSettingsPacket(ByteBuf buf) {
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
        runLaterServer(() -> GameManager.get().setSettings(settings));
    }
}
