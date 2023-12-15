package cn.leomc.pvzmultiplayer.common.networking.packet.world;

import cn.leomc.pvzmultiplayer.client.SceneManager;
import cn.leomc.pvzmultiplayer.client.scene.CollaborativeGameScene;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public record ClientboundShovelPacket(boolean success) implements Packet {

    public ClientboundShovelPacket(ByteBuf buf) {
        this(buf.readBoolean());
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeBoolean(success);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterClient(() -> {
            if (SceneManager.get().getCurrentScene() instanceof CollaborativeGameScene scene)
                scene.shovelResult(success);
        });
    }
}
