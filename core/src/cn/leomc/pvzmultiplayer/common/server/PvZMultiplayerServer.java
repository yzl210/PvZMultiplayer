package cn.leomc.pvzmultiplayer.common.server;

import cn.leomc.pvzmultiplayer.common.EventLoop;
import cn.leomc.pvzmultiplayer.common.game.GameManager;

public class PvZMultiplayerServer extends EventLoop {

    private static PvZMultiplayerServer INSTANCE;

    private Thread serverThread;
    private ServerManager serverManager;
    private final GameManager gameManager = new GameManager();

    public static void start() {
        if (INSTANCE != null)
            throw new IllegalStateException("Server is already running!");

        INSTANCE = new PvZMultiplayerServer();

        INSTANCE.serverThread = INSTANCE.createThread("Server Thread");
        INSTANCE.serverThread.start();
    }

    public static void waitForStart() {
        while (INSTANCE == null || INSTANCE.serverManager == null || !INSTANCE.serverManager.isRunning()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public static void stop() {
        if (INSTANCE == null)
            return;

        INSTANCE.serverManager.stop();
        INSTANCE.serverThread.interrupt();
        INSTANCE = null;
    }

    @Override
    public void tick() {
        if (serverManager == null)
            serverManager = new ServerManager();
        gameManager.tick();
        super.tick();
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public static PvZMultiplayerServer getInstance() {
        return INSTANCE;
    }

}
