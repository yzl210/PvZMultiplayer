package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.common.game.GameManager;
import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public record ServerboundGameSettingsPacket(GameSettings settings) implements Packet {

    public ServerboundGameSettingsPacket(ByteBuf buf) {
        this(GameSettings.GameMode.values()[buf.readInt()].read(buf));
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
