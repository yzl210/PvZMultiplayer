package cn.leomc.pvzmultiplayer.client;

import cn.leomc.pvzmultiplayer.client.networking.ClientConnection;
import cn.leomc.pvzmultiplayer.client.scene.GameScene;
import cn.leomc.pvzmultiplayer.client.scene.LobbyScene;
import cn.leomc.pvzmultiplayer.client.scene.MainMenuScene;
import cn.leomc.pvzmultiplayer.common.EventLoop;
import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.game.GameState;
import cn.leomc.pvzmultiplayer.common.networking.packet.ServerboundGameSettingsPacket;
import cn.leomc.pvzmultiplayer.common.server.PvZMultiplayerServer;

import java.util.List;

public class ClientGameManager extends EventLoop {

    private final Thread clientTickThread = createThread("Client Tick Thread");

    private String name = "Player";
    private ClientConnection connection;
    private boolean isHost = false;

    private boolean initialized = false;
    private GameSettings gameSettings = new GameSettings();
    private GameState state;
    private List<String> playerList;

    public ClientGameManager() {
        clientTickThread.start();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ClientGameManager get() {
        return PvZMultiplayerClient.getInstance().getGameManager();
    }

    public void host() {
        PvZMultiplayerServer.start();
        PvZMultiplayerServer.waitForStart();
        connect("127.0.0.1");
        isHost = true;
    }

    public void connect(String address) {
        if (isConnected())
            throw new IllegalStateException("Already connected");
        System.out.println("Connecting to " + address);
        connection = new ClientConnection(address);
        isHost = false;
    }

    public boolean isConnected() {
        return connection != null && connection.isConnected();
    }

    public ClientConnection getConnection() {
        return connection;
    }

    public void disconnect() {
        PvZMultiplayerClient.getInstance().runLater(() -> {
            if (SceneManager.get().getCurrentScene() instanceof LobbyScene)
                SceneManager.get().setScene(new MainMenuScene());
        });
        if (!isConnected())
            return;
        connection.disconnect();
        connection = null;
        if (isHost)
            PvZMultiplayerServer.stop();
    }

    public boolean isHost() {
        return isHost;
    }

    public Thread getClientTickThread() {
        return clientTickThread;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
        System.out.println("Initialized");
    }

    public GameState getState() {
        return state;
    }

    public void changeState(GameState state) {
        this.state = state;
        PvZMultiplayerClient.getInstance().runLater(() -> {
            switch (state) {
                case LOBBY -> SceneManager.get().setScene(new LobbyScene());
                case IN_GAME -> SceneManager.get().setScene(new GameScene());
            }
        });
    }

    public void setPlayerList(List<String> playerList) {
        this.playerList = playerList;
    }

    public List<String> getPlayerList() {
        return playerList;
    }

    public void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public void sendGameSettings() {
        connection.sendPacket(new ServerboundGameSettingsPacket(gameSettings));
    }

}
