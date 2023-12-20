package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.common.networking.Packet;
import cn.leomc.pvzmultiplayer.common.networking.util.ByteBufUtils;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;
import com.badlogic.gdx.math.Vector2;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public record ServerboundCursorPositionPacket(Vector2 position) implements Packet {

    public ServerboundCursorPositionPacket(ByteBuf buf) {
        this(ByteBufUtils.readVector2(buf));
    }

    @Override
    public void write(ByteBuf buf) {
        ByteBufUtils.writeVector2(buf, position);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterServer(() -> ServerManager.get().getPlayerList().setCursor(getPlayer(ctx.channel()), position));
    }
}
