package cn.leomc.pvzmultiplayer.common.game;

import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plants;
import cn.leomc.pvzmultiplayer.common.game.content.entity.zombie.ZombieType;
import cn.leomc.pvzmultiplayer.common.game.content.entity.zombie.Zombies;
import cn.leomc.pvzmultiplayer.common.game.logic.collaborative.CollaborativeGameSettings;
import cn.leomc.pvzmultiplayer.common.game.logic.competitive.CompetitiveGameSettings;
import cn.leomc.pvzmultiplayer.common.networking.util.ByteBufUtils;
import cn.leomc.pvzmultiplayer.common.server.ServerPlayer;
import cn.leomc.pvzmultiplayer.common.text.component.Component;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;

import java.util.List;

public abstract class GameSettings {

    public final List<PlantType<?>> plants = Lists.newArrayList(Plants.SUNFLOWER, Plants.PEASHOOTER, Plants.WALLNUT);
    public final List<ZombieType<?>> zombies = Lists.newArrayList(Zombies.NORMAL);

    public int initialSun = 50;
    public int sunAmount = 50;
    public boolean lazyMode = false;

    public abstract GameMode mode();

    public abstract boolean canStart();

    public void write(ByteBuf buf) {
        buf.writeInt(plants.size());
        plants.forEach(plant -> ByteBufUtils.writeString(buf, plant.id()));
        buf.writeInt(zombies.size());
        zombies.forEach(zombie -> ByteBufUtils.writeString(buf, zombie.id()));
        buf.writeInt(initialSun);
        buf.writeInt(sunAmount);
        buf.writeBoolean(lazyMode);
    }

    public void read(ByteBuf buf) {
        plants.clear();
        int size = buf.readInt();
        for (int i = 0; i < size; i++)
            plants.add((PlantType<?>) EntityManager.get(ByteBufUtils.readString(buf)));
        zombies.clear();
        size = buf.readInt();
        for (int i = 0; i < size; i++)
            zombies.add((ZombieType<?>) EntityManager.get(ByteBufUtils.readString(buf)));
        initialSun = buf.readInt();
        sunAmount = buf.readInt();
        lazyMode = buf.readBoolean();
    }


    public void onAddPlayer(ServerPlayer player) {
    }

    public void onRemovePlayer(ServerPlayer player) {
    }

    public enum GameMode {
        COLLABORATIVE,
        COMPETITIVE;

        public GameSettings createSettings() {
            return switch (this) {
                case COLLABORATIVE -> new CollaborativeGameSettings();
                case COMPETITIVE -> new CompetitiveGameSettings();
            };
        }

        public GameSettings read(ByteBuf buf) {
            return switch (this) {
                case COLLABORATIVE -> new CollaborativeGameSettings(buf);
                case COMPETITIVE -> new CompetitiveGameSettings(buf);
            };
        }

        public Component getDisplayName() {
            return switch (this) {
                case COLLABORATIVE -> Component.translatable("Collaborative");
                case COMPETITIVE -> Component.translatable("Competitive");
            };
        }

    }

}
