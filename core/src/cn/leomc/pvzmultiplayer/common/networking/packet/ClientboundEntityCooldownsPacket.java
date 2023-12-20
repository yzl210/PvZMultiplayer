package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityType;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import cn.leomc.pvzmultiplayer.common.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

public class ClientboundEntityCooldownsPacket implements Packet {

    private final Map<EntityType<?, ?>, Integer> entityCooldowns;

    public ClientboundEntityCooldownsPacket(Map<EntityType<?, ?>, Integer> entityCooldowns) {
        this.entityCooldowns = entityCooldowns;
    }

    public ClientboundEntityCooldownsPacket(ByteBuf buf) {
        this.entityCooldowns = new HashMap<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            EntityType<?, ?> entityType = EntityManager.get(ByteBufUtils.readString(buf));
            int cooldown = buf.readInt();
            entityCooldowns.put(entityType, cooldown);
        }
    }


    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(entityCooldowns.size());
        entityCooldowns.forEach((plantType, cooldown) -> {
            ByteBufUtils.writeString(buf, plantType.id());
            buf.writeInt(cooldown);
        });
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterClient(() -> ClientGameManager.get().setEntityCooldowns(entityCooldowns));
    }
}
