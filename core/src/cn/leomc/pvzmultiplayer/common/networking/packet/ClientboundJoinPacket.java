package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.SceneManager;
import cn.leomc.pvzmultiplayer.client.scene.JoinServerScene;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import cn.leomc.pvzmultiplayer.common.networking.util.ByteBufUtils;
import cn.leomc.pvzmultiplayer.common.text.component.Component;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public record ClientboundJoinPacket(boolean success, Component message) implements Packet {

    public ClientboundJoinPacket(ByteBuf buf) {
        this(buf.readBoolean(), ByteBufUtils.readComponent(buf));
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeBoolean(success);
        ByteBufUtils.writeComponent(message, buf);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterClient(() -> {
            if (success)
                ClientGameManager.get().setInitialized(true);
            else if (SceneManager.get().getCurrentScene() instanceof JoinServerScene scene)
                scene.setErrorMessage(message);
        });
    }
}
