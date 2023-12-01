package cn.leomc.pvzmultiplayer.common.game;

public enum GameState {
    LOBBY,
    IN_GAME,
    END;


    public boolean canJoin() {
        return this == LOBBY;
    }

}
