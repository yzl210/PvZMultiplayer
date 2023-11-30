package cn.leomc.pvzmultiplayer.common.game;

import io.netty.buffer.ByteBuf;

public class GameSettings {

    public GameMode mode = GameMode.COLLABORATIVE;


    public void write(ByteBuf buf) {
        buf.writeInt(mode.ordinal());
    }

    public void read(ByteBuf buf) {
        mode = GameMode.values()[buf.readInt()];
    }

    public enum GameMode {
        COLLABORATIVE,
        COMPETITIVE,
    }


}
