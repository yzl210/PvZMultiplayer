package cn.leomc.pvzmultiplayer.common.game.logic.collaborative;

import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import io.netty.buffer.ByteBuf;

public class CollaborativeGameSettings implements GameSettings {

    public CollaborativeGameSettings() {

    }

    public CollaborativeGameSettings(ByteBuf buf) {

    }

    @Override
    public GameMode mode() {
        return GameMode.COLLABORATIVE;
    }

    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public void write(ByteBuf buf) {

    }
}
