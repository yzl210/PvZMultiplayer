package cn.leomc.pvzmultiplayer.common.game;

import cn.leomc.pvzmultiplayer.common.networking.packet.ClientboundGameSettingsPacket;
import cn.leomc.pvzmultiplayer.common.server.PvZMultiplayerServer;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;

public class GameManager {

    private final GameState state = GameState.LOBBY;
    private GameSettings settings = new GameSettings();

    public void tick() {

    }

    public GameState getState() {
        return state;
    }


    public void setSettings(GameSettings settings) {
        this.settings = settings;
        ServerManager.get().sendPacket(new ClientboundGameSettingsPacket(settings));
    }

    public GameSettings getSettings() {
        return settings;
    }


    public static GameManager get() {
        return PvZMultiplayerServer.getInstance().getGameManager();
    }

}
