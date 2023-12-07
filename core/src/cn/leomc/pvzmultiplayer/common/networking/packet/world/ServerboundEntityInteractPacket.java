package cn.leomc.pvzmultiplayer.common.networking.packet.world;

import cn.leomc.pvzmultiplayer.common.game.GameManager;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import cn.leomc.pvzmultiplayer.common.game.content.world.Interactable;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public record ServerboundEntityInteractPacket(int id, int x, int y, int pointer, int button) implements Packet {

    public ServerboundEntityInteractPacket(ByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(id);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(pointer);
        buf.writeInt(button);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterServer(() -> {
            Entity entity = GameManager.get().getGameSession().getWorld().getEntity(id);
            if (entity instanceof Interactable interactable)
                interactable.onTouchDown(x, y, pointer, button);
        });
    }
}
