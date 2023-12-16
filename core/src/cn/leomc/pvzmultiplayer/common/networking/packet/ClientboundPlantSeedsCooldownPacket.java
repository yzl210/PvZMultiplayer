package cn.leomc.pvzmultiplayer.common.networking.packet;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import cn.leomc.pvzmultiplayer.common.networking.Packet;
import cn.leomc.pvzmultiplayer.common.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

public class ClientboundPlantSeedsCooldownPacket implements Packet {

    private final Map<PlantType<?>, Integer> plantSeedCooldowns;

    public ClientboundPlantSeedsCooldownPacket(Map<PlantType<?>, Integer> plantSeedCooldowns) {
        this.plantSeedCooldowns = plantSeedCooldowns;
    }

    public ClientboundPlantSeedsCooldownPacket(ByteBuf buf) {
        this.plantSeedCooldowns = new HashMap<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            PlantType<?> plantType = (PlantType<?>) EntityManager.get(ByteBufUtils.readString(buf));
            int cooldown = buf.readInt();
            plantSeedCooldowns.put(plantType, cooldown);
        }
    }


    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(plantSeedCooldowns.size());
        plantSeedCooldowns.forEach((plantType, cooldown) -> {
            ByteBufUtils.writeString(plantType.id(), buf);
            buf.writeInt(cooldown);
        });
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        runLaterClient(() -> ClientGameManager.get().setPlantSeedCooldowns(plantSeedCooldowns));
    }
}
