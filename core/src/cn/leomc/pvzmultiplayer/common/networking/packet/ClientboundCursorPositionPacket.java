package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import cn.leomc.pvzmultiplayer.common.networking.util.ByteBufUtils;
import cn.leomc.pvzmultiplayer.common.server.ServerPlayer;
import com.badlogic.gdx.math.Vector2;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

public class ClientboundCursorPositionPacket implements Packet {

    private final Map<String, Vector2> cursors;

    public ClientboundCursorPositionPacket(Map<ServerPlayer, Vector2> cursors) {
        this.cursors = new HashMap<>();
        cursors.forEach((serverPlayer, vector2) -> this.cursors.put(serverPlayer.name(), vector2));
    }

    public ClientboundCursorPositionPacket(ByteBuf buf) {
        int size = buf.readInt();
        cursors = new HashMap<>();
        for (int i = 0; i < size; i++) {
            cursors.put(ByteBufUtils.readString(buf), ByteBufUtils.readVector2(buf));
        }
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(cursors.size());
        cursors.forEach((s, vector2) -> {
            ByteBufUtils.writeString(buf, s);
            ByteBufUtils.writeVector2(buf, vector2);
        });
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterClient(() -> ClientGameManager.get().setCursors(cursors));
    }
}
