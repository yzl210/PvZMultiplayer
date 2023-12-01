package cn.leomc.pvzmultiplayer.common.game;

import cn.leomc.pvzmultiplayer.common.game.logic.collaborative.CollaborativeGameSettings;
import cn.leomc.pvzmultiplayer.common.game.logic.competitive.CompetitiveGameSettings;
import cn.leomc.pvzmultiplayer.common.server.ServerPlayer;
import cn.leomc.pvzmultiplayer.common.text.component.Component;
import io.netty.buffer.ByteBuf;

public interface GameSettings {

    GameMode mode();

    boolean canStart();
    void write(ByteBuf buf);

    default void onAddPlayer(ServerPlayer player) {
    }

    default void onRemovePlayer(ServerPlayer player) {
    }

    enum GameMode {
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
