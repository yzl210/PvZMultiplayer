package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.common.game.audio.AudioManager;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import cn.leomc.pvzmultiplayer.common.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public record ClientboundPlaySoundPacket(String sound) implements Packet {

    public ClientboundPlaySoundPacket(ByteBuf buf) {
        this(ByteBufUtils.readString(buf));
    }

    @Override
    public void write(ByteBuf buf) {
        ByteBufUtils.writeString(buf, sound);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterClient(() -> AudioManager.get(sound).play());
    }
}
