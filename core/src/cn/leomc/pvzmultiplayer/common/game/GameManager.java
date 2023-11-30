package cn.leomc.pvzmultiplayer.common.game;

import cn.leomc.pvzmultiplayer.common.server.PvZMultiplayerServer;

public class GameManager {

    private final GameState state = GameState.LOBBY;
    private final GameSettings settings = new GameSettings();

    public void tick() {

    }

    public GameState getState() {
        return state;
    }

    public GameSettings getSettings() {
        return settings;
    }


    public static GameManager get() {
        return PvZMultiplayerServer.getInstance().getGameManager();
    }

}
