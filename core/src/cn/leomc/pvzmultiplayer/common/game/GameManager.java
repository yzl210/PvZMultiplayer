package cn.leomc.pvzmultiplayer.common.game;

import cn.leomc.pvzmultiplayer.common.game.logic.GameSession;
import cn.leomc.pvzmultiplayer.common.game.logic.collaborative.CollaborativeGameSettings;
import cn.leomc.pvzmultiplayer.common.networking.packet.ClientboundCursorPositionPacket;
import cn.leomc.pvzmultiplayer.common.networking.packet.ClientboundGameSettingsPacket;
import cn.leomc.pvzmultiplayer.common.networking.packet.ClientboundGameStartPacket;
import cn.leomc.pvzmultiplayer.common.server.PvZMultiplayerServer;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;
import cn.leomc.pvzmultiplayer.common.server.ServerPlayer;

public class GameManager {

    private GameState state = GameState.LOBBY;
    private GameSettings settings;
    private GameSession<?> session;


    public void tick() {
        if (settings == null)
            settings = new CollaborativeGameSettings();
        if (state == GameState.IN_GAME && session != null)
            session.tick();
        else
            ServerManager.get().sendPacket(new ClientboundCursorPositionPacket(ServerManager.get().getPlayerList().getCursors()));
    }

    public void onAddPlayer(ServerPlayer player) {
        if (state.canJoin()) {
            settings.onAddPlayer(player);
            setSettings(settings);
        }
    }

    public void onRemovePlayer(ServerPlayer player) {
        if (state.canJoin()) {
            settings.onRemovePlayer(player);
            setSettings(settings);
        }
    }

    public GameState getState() {
        return state;
    }

    public void setSettings(GameSettings settings) {
        this.settings = settings;
        ServerManager.get().sendPacket(new ClientboundGameSettingsPacket(settings));
    }

    public GameSettings getSettings() {
        if (settings == null)
            settings = new CollaborativeGameSettings();
        return settings;
    }

    public GameSession<?> getGameSession() {
        return session;
    }

    public void startGame() {
        if (!settings.canStart())
            return;
        state = GameState.IN_GAME;
        session = GameSession.create(settings);
        ServerManager.get().sendPacket(new ClientboundGameStartPacket());
    }

    public static GameManager get() {
        return PvZMultiplayerServer.getInstance().getGameManager();
    }

}
