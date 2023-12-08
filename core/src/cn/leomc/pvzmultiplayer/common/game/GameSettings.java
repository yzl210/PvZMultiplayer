package cn.leomc.pvzmultiplayer.common.game;

import cn.leomc.pvzmultiplayer.common.game.logic.collaborative.CollaborativeGameSettings;
import cn.leomc.pvzmultiplayer.common.game.logic.competitive.CompetitiveGameSettings;
import cn.leomc.pvzmultiplayer.common.server.ServerPlayer;
import cn.leomc.pvzmultiplayer.common.text.component.Component;
import io.netty.buffer.ByteBuf;

public abstract class GameSettings {

    public int sunAmount = 50;
    public boolean lazyMode = false;

    public abstract GameMode mode();

    public abstract boolean canStart();

    public void write(ByteBuf buf) {
        buf.writeInt(sunAmount);
        buf.writeBoolean(lazyMode);
    }

    public void read(ByteBuf buf) {
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
