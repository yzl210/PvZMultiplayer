package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.client.SceneManager;
import cn.leomc.pvzmultiplayer.client.scene.CollaborativeGameScene;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ClientboundPlantPacket implements Packet {

    private final boolean success;

    public ClientboundPlantPacket(boolean success) {
        this.success = success;
    }

    public ClientboundPlantPacket(ByteBuf buf) {
        this.success = buf.readBoolean();
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeBoolean(success);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterClient(() -> {
            if (SceneManager.get().getCurrentScene() instanceof CollaborativeGameScene scene)
                scene.plantResult(success);
        });
    }
}
